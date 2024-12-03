package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MusicRepositoryCustomImpl implements MusicRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Page<MusicDto>> searchAll(String[] types, String keyword, Pageable pageable) {
        //선언
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MusicDto> query = cb.createQuery(MusicDto.class);

        Root<Music> root = query.from(Music.class);
        Join<Music, User> userJoin = root.join("user");

        List<Predicate> predicates = new ArrayList<>();

        if (types != null && types.length > 0) {
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t":
                        predicates.add(cb.like(root.get("title"), "%" + keyword + "%"));
                        break;
                    case "n":
                        predicates.add(cb.equal(userJoin.get("nickname"), keyword));  // 정확히 일치하는 nickname 검색
                        break;
                    default:
                        // 타입이 정의되지 않은 경우를 대비한 처리
                        break;
                }
            }
        }
        query.select(cb.construct(MusicDto.class
                ,root.get("musicId"),root.get("userId"),root.get("title"),root.get("filePath"),root.get("price"),root.get("description"),root.get("soundType")
                ,userJoin.get("nickname")
                ,root.get("create_date"),root.get("modify_date")
                ));

        // 조건 추가
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // 쿼리 실행
        TypedQuery<MusicDto> typedQuery = entityManager.createQuery(query);
        // 페이징 처리
        int totalRows = typedQuery.getResultList().size(); // 총 개수 조회
        typedQuery.setFirstResult((int) pageable.getOffset()); // 시작 인덱스
        typedQuery.setMaxResults(pageable.getPageSize());     // 페이지 크기

        // 데이터 반환
        return Optional.of(new PageImpl<>(typedQuery.getResultList(), pageable, totalRows));

    }
}

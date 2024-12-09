package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
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

public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Page<AlbumDto>> searchAll(String[] types, String keyword, Pageable pageable) {
        // 선언
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlbumDto> query =cb.createQuery(AlbumDto.class);

        //Root
        Root<Album> root = query.from(Album.class);
        //조건
        List<Predicate> predicates = new ArrayList<>();

        if (types != null && types.length > 0) {
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "t":
                        predicates.add(cb.like(root.get("albumName"), "%" + keyword + "%"));  // "title" -> albumName
                        break;
                    case "n":
                        predicates.add(cb.equal(root.get("nickname"), keyword));  // 정확히 일치하는 nickname 검색
                        break;
                    default:
                        break;
                }
            }
        }

        query.select(cb.construct(AlbumDto.class
                ,root.get("albumId"),root.get("userId"),root.get("albumName"),root.get("albumArtPath"),root.get("description")
                ,root.get("nickname"),root.get("create_date"),  // BaseEntityDto의 create_date
                root.get("modify_date")
        ));
        // 조건 추가
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // 쿼리 실행
        TypedQuery<AlbumDto> typedQuery = entityManager.createQuery(query);
        // 페이징 처리
        int totalRows = typedQuery.getResultList().size(); // 총 개수 조회
        typedQuery.setFirstResult((int) pageable.getOffset()); // 시작 인덱스
        typedQuery.setMaxResults(pageable.getPageSize());     // 페이지 크기

        // 데이터 반환
        return Optional.of(new PageImpl<>(typedQuery.getResultList(), pageable, totalRows));
    }
}

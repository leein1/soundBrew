package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
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
    public Optional<SearchTotalResultDto> soundOne(String nickname, String title) {
        //선언
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchTotalResultDto> query = cb.createQuery(SearchTotalResultDto.class);

        Root<Music> root = query.from(Music.class);
        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = root.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = root.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = root.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("nickname"), nickname));
        predicates.add(cb.equal(root.get("title"), title));

        query.select(cb.construct(SearchTotalResultDto.class
                ,root.get("musicId"),root.get("title"),root.get("filePath"),root.get("price"),root.get("description")
                ,root.get("nickname"),
                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName"))),
                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName"))),
                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName"))),
                root.get("create_date"),root.get("modify_date")
        ));

        // 조건 추가
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        query.groupBy(
                root.get("musicId"), root.get("title"), root.get("filePath"), root.get("price"), root.get("description"),
                root.get("nickname"));

        // 쿼리 실행
        TypedQuery<SearchTotalResultDto> typedQuery = entityManager.createQuery(query);
        // 데이터 반환
        List<SearchTotalResultDto> results = typedQuery.getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<SearchTotalResultDto> soundOne(String nickname, int id) {
        //선언
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchTotalResultDto> query = cb.createQuery(SearchTotalResultDto.class);

        Root<Music> root = query.from(Music.class);
        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = root.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = root.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = root.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("nickname"), nickname));
        predicates.add(cb.equal(root.get("musicId"), id));

        query.select(cb.construct(SearchTotalResultDto.class
                ,root.get("musicId"),root.get("title"),root.get("filePath"),root.get("price"),root.get("description")
                ,root.get("nickname"),
                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName"))),
                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName"))),
                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName"))),
                root.get("create_date"),root.get("modify_date")
        ));

        // 조건 추가
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        query.groupBy(
                root.get("musicId"), root.get("title"), root.get("filePath"), root.get("price"), root.get("description"),
                root.get("nickname"));

        // 쿼리 실행
        TypedQuery<SearchTotalResultDto> typedQuery = entityManager.createQuery(query);
        // 데이터 반환
        List<SearchTotalResultDto> results = typedQuery.getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}

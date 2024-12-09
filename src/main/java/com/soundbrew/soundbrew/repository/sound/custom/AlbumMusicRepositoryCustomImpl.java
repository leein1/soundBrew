package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.domain.*;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.sound.SearchAlbumResultDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

public class AlbumMusicRepositoryCustomImpl implements AlbumMusicRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Page<SearchTotalResultDto>> search(RequestDto searchRequestDto) {
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchTotalResultDto> query = cb.createQuery(SearchTotalResultDto.class);

        Root<AlbumMusic> root = query.from(AlbumMusic.class);
        Join<AlbumMusic, Album> albumJoin = root.join("album");
        Join<AlbumMusic, Music> musicJoin = root.join("music");
        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = musicJoin.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = musicJoin.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = musicJoin.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        // 조건 추가
        List<Predicate> predicates = new ArrayList<>();
        if(searchRequestDto.getType() != null) {
            for (String type : searchRequestDto.getType()) {
                switch (type.toLowerCase()) {
                    case "t":
                        predicates.add(cb.like(root.get("albumName"), "%" + searchRequestDto.getKeyword() + "%"));  // "title" -> albumName
                        break;
                    case "n":
                        predicates.add(cb.equal(root.get("nickname"), searchRequestDto.getKeyword()));  // 정확히 일치하는 nickname 검색
                        break;
                    default:
                        break;
                }
            }
        }

        if (searchRequestDto.getMore() != null && !searchRequestDto.getMore().isEmpty()) {
            for (Map.Entry<String, String> entry : searchRequestDto.getMore().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key.toLowerCase()) {
                    case "instrument": instruments = Arrays.asList(value.split(","));break;
                    case "genre": genres = Arrays.asList(value.split(","));break;
                    case "mood": moods = Arrays.asList(value.split(","));break;
                }
            }
        }

        // SELECT 쿼리 구성
        query.select(cb.construct(SearchTotalResultDto.class,
            albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"),
            musicJoin.get("musicId"), musicJoin.get("title"), musicJoin.get("filePath"), musicJoin.get("price"), musicJoin.get("description"),
                albumJoin.get("nickname"),
            cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName"))),
            cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName"))),
            cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName"))),
            musicJoin.get("create_date"),musicJoin.get("modify_date")
        ));

        query.where(predicates.toArray(new Predicate[0]));

        // GROUP BY 절 추가
        query.groupBy(albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"),
            musicJoin.get("musicId"), musicJoin.get("title"), musicJoin.get("filePath"), musicJoin.get("price"), musicJoin.get("description"),
                albumJoin.get("nickname"));

        // HAVING 절 조건 추가 (중복 제거)
        List<Predicate> havingPredicates = new ArrayList<>();

        // 악기 조건
        if (!instruments.isEmpty()) {
            for (String tag : instruments) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName")))), 0));
            }
        }

        // 무드 조건
        if (!moods.isEmpty()) {
            for (String tag : moods) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName")))), 0));
            }
        }

        // 장르 조건
        if (!genres.isEmpty()) {
            for (String tag : genres) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName")))), 0));
            }
        }

        // 조건을 하나로 묶어서 HAVING 절에 추가
        if (!havingPredicates.isEmpty()) {
            query.having(cb.and(havingPredicates.toArray(new Predicate[0])));
        }

        TypedQuery<SearchTotalResultDto> typedQuery = entityManager.createQuery(query);

        // 페이징 처리
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) searchRequestDto.getPageable().getOffset());
        typedQuery.setMaxResults(searchRequestDto.getPageable().getPageSize());

        return Optional.of(new PageImpl<>(typedQuery.getResultList(), searchRequestDto.getPageable(), totalRows));
    }

    @Override
    public Optional<Page<SearchAlbumResultDto>> albums(RequestDto searchRequestDto) {
        List<String> instruments = new ArrayList<>();
        List<String> moods = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        //선언
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchAlbumResultDto> query = cb.createQuery(SearchAlbumResultDto.class);

        //조인
        Root<AlbumMusic> root = query.from(AlbumMusic.class);
        Join<AlbumMusic, Album> albumJoin = root.join("album");
        Join<AlbumMusic, Music> musicJoin = root.join("music");

        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = musicJoin.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = musicJoin.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = musicJoin.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        // 조건 추가
        List<Predicate> predicates = new ArrayList<>();
        if(searchRequestDto.getType() != null) {
            for (String type : searchRequestDto.getType()) {
                switch (type.toLowerCase()) {
                    case "t":
                        predicates.add(cb.like(root.get("albumName"), "%" + searchRequestDto.getKeyword() + "%"));  // "title" -> albumName
                        break;
                    case "n":
                        predicates.add(cb.equal(root.get("nickname"), searchRequestDto.getKeyword()));  // 정확히 일치하는 nickname 검색
                        break;
                    default:
                        break;
                }
            }
        }

        if (searchRequestDto.getMore() != null && !searchRequestDto.getMore().isEmpty()) {
            for (Map.Entry<String, String> entry : searchRequestDto.getMore().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key.toLowerCase()) {
                    case "instrument": instruments = Arrays.asList(value.split(","));break;
                    case "genre": genres = Arrays.asList(value.split(","));break;
                    case "mood": moods = Arrays.asList(value.split(","));break;
                }
            }
        }
        //==
        //쿼리선택
        query.select(cb.construct(SearchAlbumResultDto.class,
                albumJoin.get("albumId"),albumJoin.get("albumName"),albumJoin.get("albumArtPath"),albumJoin.get("description")
                ,albumJoin.get("nickname")
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, instrumentTagJoin.get("instrumentTagName")))
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, moodTagJoin.get("moodTagName")))
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, genreTagJoin.get("genreTagName")))
                ,albumJoin.get("create_date"),albumJoin.get("modify_date")
        ));

        //조건 붙이기
        query.where(predicates.toArray(new Predicate[0]));

        // GROUP BY 절 추가
        query.groupBy(
                albumJoin.get("albumId"),
                albumJoin.get("albumName"),
                albumJoin.get("albumArtPath"),
                albumJoin.get("description"),
                albumJoin.get("nickname")
        );
        //==
        // HAVING 절 조건 추가 (중복 제거)
        List<Predicate> havingPredicates = new ArrayList<>();

        // 악기 조건
        if (!instruments.isEmpty()) {
            for (String tag : instruments) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName")))), 0));
            }
        }

        // 무드 조건
        if (!moods.isEmpty()) {
            for (String tag : moods) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName")))), 0));
            }
        }

        // 장르 조건
        if (!genres.isEmpty()) {
            for (String tag : genres) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName")))), 0));
            }
        }

        // 조건을 하나로 묶어서 HAVING 절에 추가
        if (!havingPredicates.isEmpty()) {
            query.having(cb.and(havingPredicates.toArray(new Predicate[0])));
        }
        //==
        // 페이징 설정
        TypedQuery<SearchAlbumResultDto> typedQuery = entityManager.createQuery(query);
        // 페이징 처리
        int totalRows = typedQuery.getResultList().size(); // 총 개수 조회
        typedQuery.setFirstResult((int) searchRequestDto.getPageable().getOffset()); // 시작 인덱스
        typedQuery.setMaxResults(searchRequestDto.getPageable().getPageSize());     // 페이지 크기

        return Optional.of(new PageImpl<>(typedQuery.getResultList(), searchRequestDto.getPageable(), totalRows));
    }
}

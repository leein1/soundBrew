package com.soundbrew.soundbrew.repository.sound.custom;

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

public class AlbumMusicRepositoryCustomImpl implements AlbumMusicRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    private List<String> instruments = new ArrayList<>();
    private List<String> moods = new ArrayList<>();
    private List<String> genres = new ArrayList<>();

    @Override
    public Optional<Page<SearchTotalResultDto>> search(RequestDto searchRequestDto, String searchType) {
        //최초 작업준비
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchTotalResultDto> query = cb.createQuery(SearchTotalResultDto.class);

        //join
        Root<AlbumMusic> root = query.from(AlbumMusic.class);
        Join<AlbumMusic, Album> albumJoin = root.join("album");
        Join<AlbumMusic, Music> musicJoin = root.join("music");
        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = musicJoin.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = musicJoin.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = musicJoin.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        // 검색 조건에 맞는 필터링 로직을 생성 (ex: 음원 타이틀, 앨범 타이틀 등)
        List<Predicate> searchPredicates = createSearchPredicates(cb, root, albumJoin, musicJoin, searchRequestDto, searchType);
        // 검색에 필요한 DTO 필드를 선택하는 설정
        configureSelectQuery(query, cb, albumJoin, musicJoin, instrumentTagJoin, moodTagJoin, genreTagJoin, searchType);
        // 준비된 검색 조건들을 쿼리에 적용
        query.where(searchPredicates.toArray(new Predicate[0]));
        // 그룹화 조건 설정
        configureGroupBy(query, cb, albumJoin, musicJoin, searchType);
        // 태그 관련 파라미터 처리 (ex: instrument, genre, mood)
        tagRequestPredicates(searchRequestDto);
        // 각 태그 파라미터에 대해 HAVING 조건을 생성
        List<Predicate> havingPredicates = createHavingPredicates(cb, instruments, instrumentTagJoin, "instrumentTagName");
        havingPredicates.addAll(createHavingPredicates(cb, moods, moodTagJoin, "moodTagName"));
        havingPredicates.addAll(createHavingPredicates(cb, genres, genreTagJoin, "genreTagName"));
        // 태그 필터링 조건이 있다면 HAVING 절에 추가
        if (!havingPredicates.isEmpty()) query.having(cb.and(havingPredicates.toArray(new Predicate[0])));

        // 최종 쿼리 준비 및 실행
        TypedQuery<SearchTotalResultDto> typedQuery = entityManager.createQuery(query);

        // 페이징
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult   ((int) searchRequestDto.getPageable().getOffset());
        typedQuery.setMaxResults(searchRequestDto.getPageable().getPageSize());

        return Optional.of(new PageImpl<>(typedQuery.getResultList(), searchRequestDto.getPageable(), totalRows));
    }

    @Override
    public Optional<Page<SearchTotalResultDto>> getAlbumOne(String nickname, String albumName,RequestDto requestDto) {
        //최초 작업준비
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchTotalResultDto> query = cb.createQuery(SearchTotalResultDto.class);

        //join
        Root<AlbumMusic> root = query.from(AlbumMusic.class);
        Join<AlbumMusic, Album> albumJoin = root.join("album");
        Join<AlbumMusic, Music> musicJoin = root.join("music");
        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = musicJoin.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = musicJoin.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = musicJoin.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(albumJoin.get("nickname"), nickname));
        predicates.add(cb.equal(albumJoin.get("albumName"), albumName));

        // 검색에 필요한 DTO 필드를 선택하는 설정
        configureSelectQuery(query, cb, albumJoin, musicJoin, instrumentTagJoin, moodTagJoin, genreTagJoin, "sound");
        // 준비된 검색 조건들을 쿼리에 적용
        query.where(predicates.toArray(new Predicate[0]));
        // 그룹화 조건 설정
        configureGroupBy(query, cb, albumJoin, musicJoin, "sound");

        // 최종 쿼리 준비 및 실행
        TypedQuery<SearchTotalResultDto> typedQuery = entityManager.createQuery(query);

        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) requestDto.getPageable().getOffset());
        typedQuery.setMaxResults(requestDto.getPageable().getPageSize());

        return Optional.of(new PageImpl<>(typedQuery.getResultList(), requestDto.getPageable(), totalRows));
    }

    private void configureSelectQuery(CriteriaQuery<SearchTotalResultDto> query, CriteriaBuilder cb, Join<AlbumMusic, Album> albumJoin, Join<AlbumMusic, Music> musicJoin,
                                      Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin, Join<MusicMoodTag, MoodTag> moodTagJoin, Join<MusicGenreTag, GenreTag> genreTagJoin, String searchType) {
        switch (searchType.toLowerCase()) {
            case "sound":
                query.select(cb.construct(SearchTotalResultDto.class,
                        albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"),
                        musicJoin.get("musicId"), musicJoin.get("title"), musicJoin.get("filePath"), musicJoin.get("price"), musicJoin.get("description"),
                        albumJoin.get("nickname"),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName"))),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName"))),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName"))),
                        musicJoin.get("create_date"), musicJoin.get("modify_date")
                ));
                break;
            case "album":
                query.select(cb.construct(SearchTotalResultDto.class,
                        albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"),
                        albumJoin.get("nickname"),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName"))),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName"))),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName"))),
                        albumJoin.get("create_date"), albumJoin.get("modify_date")
                ));
                break;
        }
    }

    private void configureGroupBy(CriteriaQuery<SearchTotalResultDto> query, CriteriaBuilder cb, Join<AlbumMusic, Album> albumJoin, Join<AlbumMusic, Music> musicJoin, String searchType) {
        switch (searchType.toLowerCase()) {
            case "sound":
                query.groupBy(albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"),
                        musicJoin.get("musicId"), musicJoin.get("title"), musicJoin.get("filePath"), musicJoin.get("price"), musicJoin.get("description"),
                        albumJoin.get("nickname"));
                break;
            case "album":
                query.groupBy(albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"), albumJoin.get("nickname"));
                break;
        }
    }

    private List<Predicate> createSearchPredicates(CriteriaBuilder cb, Root<AlbumMusic> root, Join<AlbumMusic, Album> albumJoin, Join<AlbumMusic, Music> musicJoin,
                                                   RequestDto searchRequestDto, String searchType) {
        List<Predicate> predicates = new ArrayList<>();
        if (searchRequestDto.getType() == null) return predicates;

        List<String> types = List.of(searchRequestDto.getType());
        for (String type : types) {
            switch (searchType.toLowerCase()) {
                case "sound":
                    addSoundPredicates(cb, musicJoin, predicates, type, searchRequestDto.getKeyword());
                    break;

                case "album":
                    addAlbumPredicates(cb, albumJoin, predicates, type, searchRequestDto.getKeyword());
                    break;

                default:
                    break;
            }
        }
        return predicates;
    }

    private void addSoundPredicates(CriteriaBuilder cb, Join<AlbumMusic, Music> musicJoin, List<Predicate> predicates, String type, String keyword) {
        switch (type.toLowerCase()) {
            case "t":
                predicates.add(cb.like(musicJoin.get("title"), "%" + keyword + "%"));  // title 검색
                break;
            case "n":
                predicates.add(cb.equal(musicJoin.get("nickname"), keyword));  // nickname 검색
                break;
            default:
                break;
        }
    }

    private void addAlbumPredicates(CriteriaBuilder cb, Join<AlbumMusic, Album> albumJoin, List<Predicate> predicates, String type, String keyword) {
        switch (type.toLowerCase()) {
            case "t":
                predicates.add(cb.like(albumJoin.get("albumName"), "%" + keyword + "%"));  // albumName 검색
                break;
            case "n":
                predicates.add(cb.equal(albumJoin.get("nickname"), keyword));  // nickname 검색
                break;
            default:
                break;
        }
    }

    private void tagRequestPredicates(RequestDto searchRequestDto){
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
    }

    private List<Predicate> createHavingPredicates(CriteriaBuilder cb, List<String> tags, Join<?, ?> tagJoin, String tagField) {
        List<Predicate> predicates = new ArrayList<>();
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                predicates.add(cb.gt(
                        cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                                cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, tagJoin.get(tagField)))
                        ), 0)
                );
            }
        }
        return predicates;
    }

}

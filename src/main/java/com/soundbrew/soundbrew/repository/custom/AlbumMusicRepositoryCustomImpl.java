package com.soundbrew.soundbrew.repository.custom;

import com.soundbrew.soundbrew.domain.*;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

public class AlbumMusicRepositoryCustomImpl implements AlbumMusicRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AlbumMusicDto> search(String nickname, Integer musicId, Integer albumId, List<String> instTags, List<String> moodTags, List<String> genreTags, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlbumMusicDto> query = cb.createQuery(AlbumMusicDto.class);

        Root<AlbumMusic> root = query.from(AlbumMusic.class);
        Join<AlbumMusic, Album> albumJoin = root.join("album");
        Join<AlbumMusic, Music> musicJoin = root.join("music");
        Join<AlbumMusic, User> userJoin = root.join("user");
        Join<Music, MusicInstrumentTag> musicInstrumentTagJoin = musicJoin.join("musicInstrumentTag", JoinType.LEFT);
        Join<MusicInstrumentTag, InstrumentTag> instrumentTagJoin = musicInstrumentTagJoin.join("instrumentTag", JoinType.LEFT);
        Join<Music, MusicMoodTag> musicMoodTagJoin = musicJoin.join("musicMoodTag", JoinType.LEFT);
        Join<MusicMoodTag, MoodTag> moodTagJoin = musicMoodTagJoin.join("moodTag", JoinType.LEFT);
        Join<Music, MusicGenreTag> musicGenreTagJoin = musicJoin.join("musicGenreTag", JoinType.LEFT);
        Join<MusicGenreTag, GenreTag> genreTagJoin = musicGenreTagJoin.join("genreTag", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        if (nickname != null) {predicates.add(cb.equal(userJoin.get("nickname"), nickname));}
        if (musicId != null) { predicates.add(cb.equal(musicJoin.get("music_id"), musicId));}
        if (albumId != null) { predicates.add(cb.equal(albumJoin.get("album_id"), albumId));}

        query.select(cb.construct(AlbumMusicDto.class,
                albumJoin.get("album_id"),albumJoin.get("album_name"),albumJoin.get("album_art_path"),albumJoin.get("description")
                ,musicJoin.get("music_id"),musicJoin.get("title"),musicJoin.get("file_path"),musicJoin.get("price"),musicJoin.get("description")
                ,userJoin.get("nickname")
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, instrumentTagJoin.get("instrument_tag_name")))
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, moodTagJoin.get("mood_tag_name")))
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, genreTagJoin.get("genre_tag_name")))
        ));

        query.where(predicates.toArray(new Predicate[0]));

        // GROUP BY 절 추가
        query.groupBy(albumJoin.get("album_id"), albumJoin.get("album_name"), albumJoin.get("album_art_path"), albumJoin.get("description"),
                musicJoin.get("music_id"), musicJoin.get("title"), musicJoin.get("file_path"), musicJoin.get("price"), musicJoin.get("description"),
                userJoin.get("nickname"));

        // FIND_IN_SET 함수는 GROUP_CONCAT된 문자열 내에서 특정 태그가 존재하는지 검사함 즉, 모든 instTags가 포함된 곡만 필터링
        if (instTags != null && !instTags.isEmpty()) {
            List<Predicate> havingPredicates = new ArrayList<>();
            for (String tag : instTags) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrument_tag_name")))), 0));
            }
            query.having(cb.and(havingPredicates.toArray(new Predicate[0])));
        }

        if (moodTags != null && !moodTags.isEmpty()){
            List<Predicate> havingPredicates = new ArrayList<>();
            for (String tag : moodTags){
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("mood_tag_name")))), 0));
            }
        }

        if(genreTags != null && !genreTags.isEmpty()){
            List<Predicate> havingPredicates = new ArrayList<>();
            for ( String tag : genreTags){
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genre_tag_name")))),0));
            }
        }

        TypedQuery<AlbumMusicDto> typedQuery = entityManager.createQuery(query);

        if(pageable !=null){
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
        }

        return typedQuery.getResultList();
    }
}

package com.soundbrew.soundbrew.repository.custom;

import com.soundbrew.soundbrew.domain.*;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.SoundRepositoryDto;
import com.soundbrew.soundbrew.dto.SoundRequestDto;
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
    public List<SoundRepositoryDto> search(SoundRequestDto soundRequestDto, Pageable pageable) {
        //선언
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SoundRepositoryDto> query = cb.createQuery(SoundRepositoryDto.class);

        //조인
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

        //조건
        List<Predicate> predicates = new ArrayList<>();
        if (soundRequestDto.getNickname() != null) {predicates.add(cb.equal(userJoin.get("nickname"), soundRequestDto.getNickname()));}
        if (soundRequestDto.getMusicId() != null) { predicates.add(cb.equal(musicJoin.get("musicId"), soundRequestDto.getMusicId()));}
        if (soundRequestDto.getAlbumId() != null) { predicates.add(cb.equal(albumJoin.get("albumId"), soundRequestDto.getAlbumId()));}
        // keyword로 검색도 구현해줘야함 ex) soundRequest.getKeyword로 predicates.add(cb.equal(...("music_name")) .....

        //쿼리선택
        query.select(cb.construct(SoundRepositoryDto.class,
                albumJoin.get("albumId"),albumJoin.get("albumName"),albumJoin.get("albumArtPath"),albumJoin.get("description")
                ,musicJoin.get("musicId"),musicJoin.get("title"),musicJoin.get("filePath"),musicJoin.get("price"),musicJoin.get("description")
                ,userJoin.get("nickname")
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, instrumentTagJoin.get("instrumentTagName")))
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, moodTagJoin.get("moodTagName")))
                ,cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT",String.class, genreTagJoin.get("genreTagName")))
        ));

        //조건 붙이기
        query.where(predicates.toArray(new Predicate[0]));

        // GROUP BY 절 추가
        query.groupBy(albumJoin.get("albumId"), albumJoin.get("albumName"), albumJoin.get("albumArtPath"), albumJoin.get("description"),
                musicJoin.get("musicId"), musicJoin.get("title"), musicJoin.get("filePath"), musicJoin.get("price"), musicJoin.get("description"),
                userJoin.get("nickname"));

        // FIND_IN_SET 함수는 GROUP_CONCAT된 문자열 내에서 특정 태그가 존재하는지 검사함 즉, 모든 instTags가 포함된 곡만 필터링
        if (soundRequestDto.getInstrument() != null && !soundRequestDto.getInstrument().isEmpty()) {
            List<Predicate> havingPredicates = new ArrayList<>();
            for (String tag : soundRequestDto.getInstrument()) {
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, instrumentTagJoin.get("instrumentTagName")))), 0));
            }
            query.having(cb.and(havingPredicates.toArray(new Predicate[0])));
        }

        if (soundRequestDto.getMood() != null && !soundRequestDto.getMood().isEmpty()){
            List<Predicate> havingPredicates = new ArrayList<>();
            for (String tag : soundRequestDto.getMood()){
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, moodTagJoin.get("moodTagName")))), 0));
            }
        }

        if(soundRequestDto.getGenre() != null && !soundRequestDto.getGenre().isEmpty()){
            List<Predicate> havingPredicates = new ArrayList<>();
            for ( String tag : soundRequestDto.getGenre()){
                havingPredicates.add(cb.gt(cb.function("FIND_IN_SET", Integer.class, cb.literal(tag),
                        cb.function("GROUP_CONCAT", String.class, cb.function("DISTINCT", String.class, genreTagJoin.get("genreTagName")))),0));
            }
        }
//        query.orderBy(cb.asc(musicJoin.get("music_id")));

        TypedQuery<SoundRepositoryDto> typedQuery = entityManager.createQuery(query);

        if(pageable !=null){
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
        }

        return typedQuery.getResultList();
    }
}

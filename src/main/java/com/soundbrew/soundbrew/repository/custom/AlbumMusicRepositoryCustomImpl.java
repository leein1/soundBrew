package com.soundbrew.soundbrew.repository.custom;

import com.soundbrew.soundbrew.domain.*;
import com.soundbrew.soundbrew.dto.AlbumMusicAfterDto;
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
    public List<AlbumMusicAfterDto> basicSearch(String nickname, Integer musicId, Integer albumId, Pageable pageable) {
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
                ,instrumentTagJoin.get("instrument_tag_name")
                ,moodTagJoin.get("mood_tag_name")
                ,genreTagJoin.get("genre_tag_name")
        ));

        query.where(predicates.toArray(new Predicate[0]));

        // 페이징 설정
        if(pageable != null){
            TypedQuery<AlbumMusicDto> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
            List<AlbumMusicDto> results = typedQuery.getResultList();

            return convertDto(results);
        }
        //convert 하고 convertDto return
        return convertDto(entityManager.createQuery(query).getResultList());
    }


    // convert dto
    private List<AlbumMusicAfterDto> convertDto(List<AlbumMusicDto> resultList){
        // 앨범-곡별로 그룹화하기 위한 Map (Key는 앨범ID와 곡 제목 조합)
        Map<String, AlbumMusicDto> resultMap = new HashMap<>();
        Map<String, Set<String>> instrumentTagMap = new HashMap<>();
        Map<String, Set<String>> moodTagMap = new HashMap<>();
        Map<String, Set<String>> genreTagMap = new HashMap<>();

        List<AlbumMusicAfterDto> after = new ArrayList<>();

        for (AlbumMusicDto dto : resultList) {
            String key = dto.getAlbum_id() + "_" + dto.getMusic_title();

            if (resultMap.containsKey(key)) {
                // 이미 존재하는 경우, 태그를 병합
                // 곡 제목 기반으로 각각의 태그 세트를 가져옵니다.
                instrumentTagMap.get(key).add(dto.getInstrument_tag_name());
                moodTagMap.get(key).add(dto.getMood_tag_name());
                genreTagMap.get(key).add(dto.getGenre_tag_name());

            } else {
                // 처음 추가될 때
                resultMap.put(key, dto);
                instrumentTagMap.put(key, new HashSet<>(Collections.singletonList(dto.getInstrument_tag_name())));
                moodTagMap.put(key, new HashSet<>(Collections.singletonList(dto.getMood_tag_name())));
                genreTagMap.put(key, new HashSet<>(Collections.singletonList(dto.getGenre_tag_name())));
            }
        }

// AlbumMusicAfterDto에 데이터 담기
        for (Map.Entry<String, AlbumMusicDto> entry : resultMap.entrySet()) {
            AlbumMusicDto albumMusicDto = entry.getValue();
            String key = entry.getKey();

            AlbumMusicAfterDto afterDto = new AlbumMusicAfterDto(
                    albumMusicDto.getAlbum_id(),
                    albumMusicDto.getAlbum_name(),
                    albumMusicDto.getAlbum_art_path(),
                    albumMusicDto.getAlbum_description(),
                    albumMusicDto.getMusic_id(),
                    albumMusicDto.getMusic_title(),
                    albumMusicDto.getMusic_file_path(),
                    albumMusicDto.getPrice(),
                    albumMusicDto.getMusic_description(),
                    albumMusicDto.getUser_name(),
                    new ArrayList<>(instrumentTagMap.get(key)),
                    new ArrayList<>(moodTagMap.get(key)),
                    new ArrayList<>(genreTagMap.get(key))
            );

            after.add(afterDto);
        }
        return after;
    }


}

package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import com.soundbrew.soundbrew.domain.AlbumMusic;
import com.soundbrew.soundbrew.domain.AlbumMusicId;
import com.soundbrew.soundbrew.repository.custom.AlbumMusicRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumMusicRepository extends JpaRepository<AlbumMusic, AlbumMusicId>, AlbumMusicRepositoryCustom {
//    // ** "FROM AlbumMusic am " + <<<<<- 실제 클래스 파일 명
//    // ** "JOIN am.album a " + <<<<<<- 그 클래스파일의 필드 명
//
//    // 모든 리스트 들고오기
//    // album : id, name, path, description
//    // music : title, path, price, description
//    // user : name
//    // parameter : none
//    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
//            "a.album_id, a.album_name, a.album_art_path, a.description, " +
//            "m.title, m.file_path, m.price, m.description, " +
//            "u.name, " +
//            "it.instrument_tag_name, mt.mood_tag_name, gt.genre_tag_name ) " +
//            "FROM AlbumMusic am " +
//            "JOIN am.album a " +
//            "JOIN am.music m " +
//            "JOIN am.user u " +
//            "LEFT JOIN m.musicInstrumentTag mit " +
//            "LEFT JOIN mit.instrumentTag it " +
//            "LEFT JOIN m.musicMoodTag mmt " +
//            "LEFT JOIN mmt.moodTag mt " +
//            "LEFT JOIN m.musicGenreTag mgt " +
//            "LEFT JOIN mgt.genreTag gt "
//            )
//    List<AlbumMusicDto> findAllAlbumMusic();
//
//
//    // 특정 앨범 선택 검색
//    // album : id, name, path, description
//    // music : title, path, price, description
//    // user : name
//    // parameter : album_id
//    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
//            "a.album_id, a.album_name, a.album_art_path, a.description, " +
//            "m.title, m.file_path, m.price, m.description, " +
//            "u.name, " +
//            "it.instrument_tag_name, mt.mood_tag_name, gt.genre_tag_name ) " +
//            "FROM AlbumMusic am " +
//            "JOIN am.album a " +
//            "JOIN am.music m " +
//            "JOIN am.user u " +
//            "LEFT JOIN m.musicInstrumentTag mit " +
//            "LEFT JOIN mit.instrumentTag it " +
//            "LEFT JOIN m.musicMoodTag mmt " +
//            "LEFT JOIN mmt.moodTag mt " +
//            "LEFT JOIN m.musicGenreTag mgt " +
//            "LEFT JOIN mgt.genreTag gt " +
//            "WHERE a.album_id = :album_id "
//    )
//    List<AlbumMusicDto> albumIdSearch(@Param("album_id") int album_id);
//
//
//    // 특정 아티스트 선택 검색
//    // album : id, name, path, description
//    // music : title, path, price, description
//    // user : name
//    // parameter : user_id
//    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
//            "a.album_id, a.album_name, a.album_art_path, a.description, " +
//            "m.title, m.file_path, m.price, m.description, " +
//            "u.name, " +
//            "it.instrument_tag_name, mt.mood_tag_name, gt.genre_tag_name ) " +
//            "FROM AlbumMusic am " +
//            "JOIN am.album a " +
//            "JOIN am.music m " +
//            "JOIN am.user u " +
//            "LEFT JOIN m.musicInstrumentTag mit " +
//            "LEFT JOIN mit.instrumentTag it " +
//            "LEFT JOIN m.musicMoodTag mmt " +
//            "LEFT JOIN mmt.moodTag mt " +
//            "LEFT JOIN m.musicGenreTag mgt " +
//            "LEFT JOIN mgt.genreTag gt " +
//            "WHERE u.user_id = :user_id "
//    )
//    List<AlbumMusicDto> artistIdSearch(@Param("user_id") int userId);
//
//    // 특정 음악 선택 검색
//    // album : id, name, path, description
//    // music : title, path, price, description
//    // user : name
//    // parameter : music_id
//    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
//            "a.album_id, a.album_name, a.album_art_path, a.description, " +
//            "m.title, m.file_path, m.price, m.description, " +
//            "u.name, " +
//            "it.instrument_tag_name, mt.mood_tag_name, gt.genre_tag_name ) " +
//            "FROM AlbumMusic am " +
//            "JOIN am.album a " +
//            "JOIN am.music m " +
//            "JOIN am.user u " +
//            "LEFT JOIN m.musicInstrumentTag mit " +
//            "LEFT JOIN mit.instrumentTag it " +
//            "LEFT JOIN m.musicMoodTag mmt " +
//            "LEFT JOIN mmt.moodTag mt " +
//            "LEFT JOIN m.musicGenreTag mgt " +
//            "LEFT JOIN mgt.genreTag gt " +
//            "WHERE m.music_id = :music_id "
//    )
//    List<AlbumMusicDto> musicIdSearch(@Param("music_id") int musicId);
//
//
//    //tag 그리고 criteria에 관해서는 동적 쿼리가 필요함. 나중에 작업할것.
//    //(...)

}

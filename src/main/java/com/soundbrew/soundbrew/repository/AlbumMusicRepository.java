package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import com.soundbrew.soundbrew.domain.AlbumMusic;
import com.soundbrew.soundbrew.domain.AlbumMusicId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumMusicRepository extends JpaRepository<AlbumMusic, AlbumMusicId> {


    // join 테이블 검색문
    // 모든 리스트 들고오기
    // album : id, name, path, description
    // music : title, path, price, description
    // user : name

    // ** "FROM AlbumMusic am " + <<<<<- 실제 클래스 파일 명
    // ** "JOIN am.album a " + <<<<<<- 그 클래스파일의 필드 명
    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
            "a.album_id, a.album_name, a.album_art_path, a.description, " +
            "m.title, m.file_path, m.price, m.description, " +
            "u.name," +
            "it.instrument_tag_name) " +
            "FROM AlbumMusic am " +
            "JOIN am.album a " +
            "JOIN am.music m " +
            "JOIN am.user u " +
            "LEFT JOIN m.musicInstrumentTag mit " +
            "LEFT JOIN mit.instrumentTag it"
            )
    List<AlbumMusicDto> findAllAlbumMusic();

    // join 테이블 검색문
    // album : id, name, path, description
    // music : title, path, price, description
    // user : name
    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
            "a.album_id, a.album_name, a.album_art_path, a.description, " +
            "m.title, m.file_path, m.price, m.description, " +
            "u.name," +
            "it.instrument_tag_name) " +
            "FROM AlbumMusic am " +
            "JOIN am.album a " +
            "JOIN am.music m " +
            "JOIN am.user u " +
            "LEFT JOIN m.musicInstrumentTag mit " +
            "LEFT JOIN mit.instrumentTag it " +
            "WHERE am.id = :albumMusicId")
    AlbumMusicDto joinAlbumMusic(@Param("albumMusicId") AlbumMusicId albumMusicId);


//    @Query("SELECT new com.soundbrew.soundbrew.dto.AlbumMusicDto(" +
//            "a.album_id, a.album_name, a.album_art_path, a.description, " +
//            "m.title, m.file_path, m.price, m.description, " +
//            "u.name) " +
//            "FROM AlbumMusic am " +
//            "JOIN am.album a " +
//            "JOIN am.music m " +
//            "JOIN am.user u " +
//            "WHERE a.album_id = :albumId")
//    List<AlbumMusicDto> findByAlbumId(@Param("albumId") int albumId);


}

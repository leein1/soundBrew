package com.soundbrew.soundbrew.repository.sound.N;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import com.soundbrew.soundbrew.repository.sound.custom.AlbumMusicRepositoryCustomImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class AlbumRepositoryTests {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMusicRepositoryCustomImpl albumMusicRepositoryCustom;

    // C
    @Test
    @Transactional
    void testCreate(){
        AlbumDTO dto = AlbumDTO.builder()
                .userId(2)
                .albumName("Test_album_no.12")
                .albumArtPath("/test/test/path")
                .description("test album description")
                .build();
        Album vo = dto.toEntity();

        Album showLog = albumRepository.saveAndFlush(vo);
        assertEquals("Test_album_no.12",showLog.getAlbumName());
        assertEquals("/test/test/path",showLog.getAlbumArtPath());
        assertEquals("test album description",showLog.getDescription());

        log.info(showLog);
    }

    @Test
    void testRead(){
        int albumId = 11;

        Album showLog = albumRepository.findById(albumId).orElse(null);

        log.info(showLog.getCreate_date());log.info(showLog.getCreate_date());log.info(showLog.getCreate_date());log.info(showLog.getCreate_date());log.info(showLog.getCreate_date());





        log.info(showLog);
    }

    @Test
    @Transactional
    void testUpdate(){
        int albumId = 11;

        Album modify = albumRepository.findById(albumId).orElseThrow();
        modify.update("수정된 앨범 명",modify.getDescription());

        assertEquals("수정된 앨범 명",modify.getAlbumName());

        log.info(modify);
    }

    @Test
    @Transactional
    void testCreateAndDelete() {
        // 앨범 생성
        Album album = Album.builder()
                .userId(2)
                .albumName("Test_album_no.1")
                .albumArtPath("/test/test/path")
                .description("test album description")
                .build();

        // 앨범 저장
        Album savedAlbum = albumRepository.save(album);

        // 앨범 삭제
        albumRepository.deleteById(savedAlbum.getAlbumId());

        // 삭제 후 앨범이 존재하지 않는지 확인
        assertFalse(albumRepository.existsById(savedAlbum.getAlbumId()));
    }

}

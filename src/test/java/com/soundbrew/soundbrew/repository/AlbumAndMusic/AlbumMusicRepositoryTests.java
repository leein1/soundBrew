package com.soundbrew.soundbrew.repository.AlbumAndMusic;

import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import com.soundbrew.soundbrew.domain.AlbumMusic;
import com.soundbrew.soundbrew.domain.AlbumMusicId;
import com.soundbrew.soundbrew.repository.AlbumMusicRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class AlbumMusicRepositoryTests {

    @Autowired
    private AlbumMusicRepository musicRepository;


    @Test
    @Transactional
    // join 해서 원하는 내용만 건지기
    // 음악 1개 선택을 위한 테스트
    void findCustomSearch() {
        AlbumMusicId amId = AlbumMusicId.builder()
                .album_id(11)
                .music_id(1) // 예시로 5번 음악을 조회
                .user_id(2)
                .build();

        AlbumMusicDto foundAlbumMusicDto = musicRepository.joinAlbumMusic(amId);
        log.info("==========================================");
        log.info(foundAlbumMusicDto);
        log.info("==========================================");
    }

    @Test
    @Transactional
    //조인해서 정보 다 들고오기
    // sound List를 위한 테스트
    void findCustomSearchAll() {
        List<AlbumMusicDto> foundAlbumMusicDto = musicRepository.findAllAlbumMusic();
        log.info("==========================================");
        log.info(foundAlbumMusicDto);
        log.info("==========================================");
    }

    @Test
    @Transactional
    // 정보 삽입
    // insert 중 한부분
    void insert(){
        AlbumMusicId id = AlbumMusicId.builder()
                .music_id(1)
                .album_id(1)
                .user_id(1)
                .build();


    }



}

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
        // album_id
        // 앨범 1개 선택을 위한 테스트
    void albumIdSearchTest() {
        int albumId = 11;
        List<AlbumMusicDto> test =  musicRepository.albumIdSearch(albumId);

        log.info("==========================================");
        log.info(test.toString());
        log.info("==========================================");
    }

    //조인해서 정보 다 들고오기
    // sound List를 위한 테스트
    @Test
    @Transactional
    void findCustomSearchAll() {
        List<AlbumMusicDto> foundAlbumMusicDto = musicRepository.findAllAlbumMusic();

        log.info("==========================================");
        log.info(foundAlbumMusicDto);
        log.info("==========================================");
    }

    // user_id(artist)
    // 아티스트 1명 선택을 위한 테스트
    @Test
    @Transactional
    void artistIdSearchTest(){
        int userId = 2;
        List<AlbumMusicDto> test = musicRepository.artistIdSearch(userId);

        log.info("==========================================");
        log.info(test);
        log.info("==========================================");
    }

    // music_id
    // 음악 1개 선택을 위한 테스트
    @Test
    @Transactional
    void musicIdSearchTest(){
        int musicId=1;
        List<AlbumMusicDto> test = musicRepository.musicIdSearch(musicId);

        log.info("==========================================");
        log.info(test);
        log.info("==========================================");
    }


}

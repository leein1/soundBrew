package com.soundbrew.soundbrew.repository.AlbumAndMusic;

import com.soundbrew.soundbrew.dto.AlbumMusicAfterDto;
import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import com.soundbrew.soundbrew.domain.AlbumMusic;
import com.soundbrew.soundbrew.domain.AlbumMusicId;
import com.soundbrew.soundbrew.repository.AlbumMusicRepository;
import com.soundbrew.soundbrew.repository.custom.AlbumMusicRepositoryCustomImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class AlbumMusicRepositoryTests {

    @Autowired
    private AlbumMusicRepository musicRepository;

    @Autowired
    private AlbumMusicRepositoryCustomImpl albumMusicRepositoryCustom;

    @Test
    @Transactional
    // 전체 목록
    void BasicSearch(){
        Pageable pageable = PageRequest.of(0, 2);
        List<AlbumMusicAfterDto> test = albumMusicRepositoryCustom.basicSearch(null,null,null,pageable);

        log.info("==========================================");
        log.info(test);
        log.info("==========================================");
    }

//    @Test
//    @Transactional
//    // 아티스트 nickname 검색
//    void UserNameSearch() {
//        List<AlbumMusicAfterDto> test = albumMusicRepositoryCustom.basicSearch("u_1", null, null);
//
//        log.info("==========================================");
//        log.info(test);
//        log.info("==========================================");
//    }
//
//    @Test
//    @Transactional
//    // 뮤직id 검색
//    void MusicIdSearch() {
//        List<AlbumMusicAfterDto> test = albumMusicRepositoryCustom.basicSearch(null, 11, null);
//
//        log.info("==========================================");
//        log.info(test);
//        log.info("==========================================");
//    }
//
//    @Test
//    @Transactional
//    // 앨범id 검색
//    void AlbumSearch() {
//        List<AlbumMusicAfterDto> test = albumMusicRepositoryCustom.basicSearch(null, null, null);
//
//        log.info("==========================================");
//        log.info(test);
//        log.info("==========================================");
//    }
}

package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import com.soundbrew.soundbrew.repository.custom.AlbumMusicRepositoryCustomImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class AlbumMusicRepositoryTests {

    @Autowired
    private AlbumMusicRepository musicRepository;

    @Autowired
    private AlbumMusicRepositoryCustomImpl albumMusicRepositoryCustom;

    @Test
    void testSearchByInstrumentAndMoodTags() {
        Pageable pageable = PageRequest.of(0, 2);
        List<String> instTags = Arrays.asList("violin");
        List<String> moodTags = Arrays.asList("happy");

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, null, instTags, moodTags, null, pageable);
        log.info("Case 1: violin + happy");
        log.info(result.toString());
    }

    @Test
    void testSearchByGuitarAndSadTags() {
        Pageable pageable = PageRequest.of(0, 2);
        List<String> instTags = Arrays.asList("guitar");
        List<String> moodTags = Arrays.asList("sad");

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, null, instTags, moodTags, null, pageable);
        log.info("Case 2: guitar + sad");
        log.info(result.toString());
    }

    @Test
    void testSearchByAlbumId() {
        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, 11, null, null, null, null);
        log.info("Case 3: albumId = 11, no pageing");
        log.info(result.toString());
    }

    @Test
    void testSearchByMusicId() {
        Pageable pageable = PageRequest.of(0, 2);
        Integer musicId = 10;

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, musicId, null, null, null, null, pageable);
        log.info("Case 4: musicId = 10");
        log.info(result.toString());
    }

    @Test
    void testSearchByNickname() {
        Pageable pageable = PageRequest.of(0, 2);
        String nickname = "user123";

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(nickname, null, null, null, null, null, pageable);
        log.info("Case 5: nickname = user123");
        log.info(result.toString());
    }

    @Test
    void testSearchByTagsCombination() {
        Pageable pageable = PageRequest.of(0, 2);
        List<String> instTags = Arrays.asList("drum", "bass");
        List<String> moodTags = Arrays.asList("calm");
        List<String> genreTags = Arrays.asList("rock");

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, null, instTags, moodTags, genreTags, pageable);
        log.info("Case 6: drum + bass + calm + rock");
        log.info(result.toString());
    }

    @Test
    void testSearchAllNull() {
        Pageable pageable = PageRequest.of(0, 2);

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, null, null, null, null, pageable);
        log.info("Case 7: all NULL (전체 검색)");
        log.info(result.toString());
    }

    @Test
    void testSearchByMultipleInstrumentTags() {
        Pageable pageable = PageRequest.of(0, 2);
        List<String> instTags = Arrays.asList("piano", "violin");

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, null, instTags, null, null, pageable);
        log.info("Case 8: piano + violin");
        log.info(result.toString());
    }

    @Test
    void testSearchByPagination() {
        Pageable pageable = PageRequest.of(1, 5);

        List<AlbumMusicDto> result = albumMusicRepositoryCustom.search(null, null, null, null, null, null, pageable);
        log.info("Case 9: pagination (1페이지, 5개씩)");
        log.info(result.toString());
    }

}

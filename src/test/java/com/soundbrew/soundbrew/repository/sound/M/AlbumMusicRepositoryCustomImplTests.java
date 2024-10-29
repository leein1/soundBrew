package com.soundbrew.soundbrew.repository.sound.M;

import com.soundbrew.soundbrew.dto.sound.SoundSearchResultDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.repository.custom.AlbumMusicRepositoryCustomImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class AlbumMusicRepositoryCustomImplTests {

    @Autowired
    private AlbumMusicRepositoryCustomImpl albumMusicRepositoryCustom;

    @Test
    void testJustSearch() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);

        log.info(result.toString());
    }

    @Test
    void testSearchByGuitarAndSadTags() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setInstrument(Arrays.asList("guitar"));
        requestDto.setMood(Arrays.asList("sad"));

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 2: guitar + sad");
        log.info(result.toString());
    }

    @Test
    void testSearchByAlbumId() {
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setAlbumId(11);

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, null);
        log.info(result.toString());
    }

    @Test
    void testSearchByMusicId() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setMusicId(11);

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 4: musicId = 10");
        log.info(result.toString());
    }

    @Test
    void testSearchByNickname() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setNickname("user123");

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 5: nickname = user123");
        log.info(result.toString());
    }

    @Test
    void testSearchByTagsCombination() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setInstrument(Arrays.asList("drum", "bass"));
        requestDto.setMood(Arrays.asList("calm"));
        requestDto.setGenre(Arrays.asList("rock"));

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 6: drum + bass + calm + rock");
        log.info(result.toString());
    }

    @Test
    void testSearchAllNull() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();  // 모든 필드 null

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 7: all NULL (전체 검색)");
        log.info(result.toString());
    }

    @Test
    void testSearchByMultipleInstrumentTags() {
        Pageable pageable = PageRequest.of(0, 2);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setInstrument(Arrays.asList("piano", "violin"));

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 8: piano + violin");
        log.info(result.toString());
    }

    @Test
    void testSearchByPagination() {
        Pageable pageable = PageRequest.of(0, 5);
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();  // 페이징만 적용, 나머지 필드 null

        List<SoundSearchResultDto> result = albumMusicRepositoryCustom.search(requestDto, pageable);
        log.info("Case 9: pagination (1페이지, 5개씩)");
        log.info(result.toString());
    }

}

package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.SearchRequestDto;
import com.soundbrew.soundbrew.dto.sound.SearchResultDto;
import com.soundbrew.soundbrew.dto.sound.SearchResponseDto;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class SoundSearchServiceTests {
    @Autowired
    private SoundProcessor soundProcessor;

    @Autowired
    private SoundServiceImpl soundService;

    @Test
    void testSoundSearchWithTagsAndPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        SearchRequestDto requestDto = new SearchRequestDto();
        requestDto.setInstrument(Arrays.asList("piano", "guitar"));
        requestDto.setMood(Arrays.asList("happy"));
        requestDto.setGenre(Arrays.asList("rock"));

        // When
        Optional<SearchResponseDto> result = soundService.soundSearch(requestDto, pageable);

        // Then (실제로 데이터 있냐 없냐에 따라 assert"True" or "False"
        log.info(result);

        log.info("Tested soundSearch with piano, happy, rock tags and pagination.");
    }

    @Test
    void testSoundSearchWithAlbumId() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        SearchRequestDto requestDto = new SearchRequestDto();
        requestDto.setAlbumId(11);

        // When
        Optional<SearchResponseDto> result = soundService.soundSearch(requestDto, pageable);

        // Then
        assertNotNull(result.get());
        log.info("Tested soundSearch with albumId = 11.");
    }

    @Test
    void testReplaceCommaWithSpace() {
        // Given
        SearchResultDto soundDto = new SearchResultDto();
        soundDto.setInstrumentTagName("piano,guitar");
        soundDto.setMoodTagName("happy,calm");
        soundDto.setGenreTagName("rock,pop");

        List<SearchResultDto> soundList = Arrays.asList(soundDto);

        // When
        List<SearchResultDto> result = soundProcessor.replaceCommaWithSpace(soundList);

        // Then
        assertEquals("piano guitar", result.get(0).getInstrumentTagName());
        assertEquals("happy calm", result.get(0).getMoodTagName());
        assertEquals("rock pop", result.get(0).getGenreTagName());

        log.info("Tested replaceCommaWithSpace method.");
    }

    @Test
    void testReplaceTagsToArray() {
        // Given
        SearchResultDto soundDto = new SearchResultDto();
        soundDto.setInstrumentTagName("piano,guitar");
        soundDto.setMoodTagName("happy,calm");
        soundDto.setGenreTagName("rock,pop");

        List<SearchResultDto> soundList = Arrays.asList(soundDto);

        // When
        SearchResponseDto result = soundProcessor.replaceTagsToArray(soundList);

        // Then
        assertTrue(result.getInstTag().contains("piano"));
        assertTrue(result.getInstTag().contains("guitar"));
        assertTrue(result.getMoodTag().contains("happy"));
        assertTrue(result.getMoodTag().contains("calm"));
        assertTrue(result.getGenreTag().contains("rock"));
        assertTrue(result.getGenreTag().contains("pop"));

        log.info("Tested replaceTagsToArray method.");
    }
}

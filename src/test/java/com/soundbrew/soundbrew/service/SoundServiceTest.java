package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.SoundRequestDto;
import com.soundbrew.soundbrew.dto.SoundRepositoryDto;
import com.soundbrew.soundbrew.dto.SoundServiceDto;
import com.soundbrew.soundbrew.repository.custom.AlbumMusicRepositoryCustomImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class SoundServiceTest {

    @Autowired
    private SoundService soundService;

    @Autowired
    private AlbumMusicRepositoryCustomImpl musicRepository;

    @Test
    void testSoundSearchWithTagsAndPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        SoundRequestDto requestDto = new SoundRequestDto();
        requestDto.setInstrument(Arrays.asList("piano", "guitar"));
        requestDto.setMood(Arrays.asList("happy"));
        requestDto.setGenre(Arrays.asList("rock"));

        // When
        SoundServiceDto result = soundService.soundSearch(requestDto, pageable);

        // Then (실제로 데이터 있냐 없냐에 따라 assert"True" or "False"
        assertNotNull(result);
        assertFalse(result.getInstTag().contains("piano"));
        assertFalse(result.getMoodTag().contains("happy"));
        assertFalse(result.getGenreTag().contains("rock"));

        log.info("Tested soundSearch with piano, happy, rock tags and pagination.");
    }

    @Test
    void testSoundSearchWithAlbumId() {
        // Given
        Pageable pageable = PageRequest.of(0, 2);
        SoundRequestDto requestDto = new SoundRequestDto();
        requestDto.setAlbumId(11);

        // When
        SoundServiceDto result = soundService.soundSearch(requestDto, pageable);

        // Then
        assertNotNull(result);
        log.info("Tested soundSearch with albumId = 11.");
    }

    @Test
    void testReplaceCommaWithSpace() {
        // Given
        SoundRepositoryDto soundDto = new SoundRepositoryDto();
        soundDto.setInstrumentTagName("piano,guitar");
        soundDto.setMoodTagName("happy,calm");
        soundDto.setGenreTagName("rock,pop");

        List<SoundRepositoryDto> soundList = Arrays.asList(soundDto);

        // When
        List<SoundRepositoryDto> result = soundService.replaceCommaWithSpace(soundList);

        // Then
        assertEquals("piano guitar", result.get(0).getInstrumentTagName());
        assertEquals("happy calm", result.get(0).getMoodTagName());
        assertEquals("rock pop", result.get(0).getGenreTagName());

        log.info("Tested replaceCommaWithSpace method.");
    }

    @Test
    void testReplaceTagsToArray() {
        // Given
        SoundRepositoryDto soundDto = new SoundRepositoryDto();
        soundDto.setInstrumentTagName("piano,guitar");
        soundDto.setMoodTagName("happy,calm");
        soundDto.setGenreTagName("rock,pop");

        List<SoundRepositoryDto> soundList = Arrays.asList(soundDto);

        // When
        SoundServiceDto result = soundService.replaceTagsToArray(soundList);

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

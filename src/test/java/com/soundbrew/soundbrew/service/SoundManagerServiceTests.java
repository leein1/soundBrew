package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.SoundCreateDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.repository.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundManagerService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Log4j2
public class SoundManagerServiceTests {
    @Autowired private MusicRepository musicRepository;
    @Autowired private InstrumentTagRepository instrumentTagRepository;
    @Autowired private GenreTagRepository genreTagRepository;
    @Autowired private MoodTagRepository moodTagRepository;

    @Autowired private MusicInstrumentTagRepository musicInstrumentTagRepository;
    @Autowired private MusicMoodTagRepository musicMoodTagRepository;
    @Autowired private MusicGenreTagRepository musicGenreTagRepository;
    @Autowired private AlbumRepository albumRepository;

    @Autowired private SoundManagerService soundManagerService;

    private Music music;


    @BeforeEach
    void insert(){
        //컨트롤러에서 넘어온 리퀘스트
        SoundSearchRequestDto request = SoundSearchRequestDto.builder()
                .mood(new ArrayList<>(Arrays.asList("sad", "sadtest")))                   // Mood 태그 추가
                .instrument(new ArrayList<>(Arrays.asList("snare", "snaretest2")))  // Instrument 태그 추가
                .genre(new ArrayList<>(Arrays.asList("rock", "rocktest")))                   // Genre 태그 추가
                .build();

        //음악
        music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        //태그들
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();
        moodTagRepository.save(moodTag);
        MoodTag moodTag2 = MoodTag.builder()
                .moodTagName("sadtest")
                .build();
        moodTagRepository.save(moodTag2);

        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("snare")
                .build();
        instrumentTagRepository.save(instrumentTag);
        InstrumentTag instrumentTag2 = InstrumentTag.builder()
                .instrumentTagName("snaretest2")
                .build();
        instrumentTagRepository.save(instrumentTag2);

        GenreTag genreTag= GenreTag.builder()
                .genreTagName("rock")
                .build();
        genreTagRepository.save(genreTag);
        GenreTag genreTag2= GenreTag.builder()
                .genreTagName("rocktest")
                .build();
        genreTagRepository.save(genreTag2);

        // 중간 테이블 리스트(여러개)에 넣을 준비
        List<MusicInstrumentTag> instrumentTags = new ArrayList<>();
        List<MusicGenreTag> genreTags = new ArrayList<>();
        List<MusicMoodTag> moodTags = new ArrayList<>();

        for(String tagName : request.getInstrument()){
            InstrumentTag dbTagName = instrumentTagRepository.findByInstrumentTagName(tagName).orElseThrow();

            MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                    .id (new MusicInstrumentTagId(music.getMusicId(), dbTagName.getInstrumentTagId()))
                    .music(music)
                    .instrumentTag(dbTagName)
                    .build();
            instrumentTags.add(musicInstrumentTag);
        }
        for (String tagName : request.getMood()){
            MoodTag dbMoodTag = moodTagRepository.findByMoodTagName(tagName).orElseThrow();

            MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                    .id(new MusicMoodTagId(music.getMusicId(), dbMoodTag.getMoodTagId()))
                    .music(music)
                    .moodTag(dbMoodTag)
                    .build();
            moodTags.add(musicMoodTag);
        }
        for(String tagName : request.getGenre()){
            GenreTag dbGenreTag = genreTagRepository.findByGenreTagName(tagName).orElseThrow();

            MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                    .id(new MusicGenreTagId(music.getMusicId(), dbGenreTag.getGenreTagId()))
                    .music(music)
                    .genreTag(dbGenreTag)
                    .build();
            genreTags.add(musicGenreTag);
        }

        //중간테이블들 save
        musicInstrumentTagRepository.saveAll(instrumentTags);
        musicMoodTagRepository.saveAll(moodTags);
        musicGenreTagRepository.saveAll(genreTags);

        assertNotNull(musicInstrumentTagRepository.findByIdMusicId(music.getMusicId()));
        assertNotNull(musicMoodTagRepository.findByIdMusicId(music.getMusicId()));
        assertNotNull(musicGenreTagRepository.findByIdMusicId(music.getMusicId()));
    }

    @AfterEach
    void delete(){
        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.getMusicId());
        assertTrue(musicInstrumentTagRepository.findByIdMusicId(music.getMusicId()).isEmpty());
        assertTrue(musicMoodTagRepository.findByIdMusicId(music.getMusicId()).isEmpty());
        assertTrue(musicGenreTagRepository.findByIdMusicId(music.getMusicId()).isEmpty());

    }



    @Test
    @Transactional
    void testUpdateMusicTags() {
        // Given
        Music music = Music.builder()
                .title("Test Music")
                .filePath("/path/test_music.mp3")
                .price(10)
                .description("Test Music Description")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        SoundCreateDto request = SoundCreateDto.builder()
                .mood(Arrays.asList("happy2", "energetic2"))
                .instrument(Arrays.asList("piano2", "guitar2"))
                .genre(Arrays.asList("pop2", "jazz2"))
                .build();

        // Mood, Instrument, Genre 태그 생성
        MoodTag happy = MoodTag.builder().moodTagName("happy2").build();
        MoodTag energetic = MoodTag.builder().moodTagName("energetic2").build();
        moodTagRepository.saveAll(Arrays.asList(happy, energetic));

        InstrumentTag piano = InstrumentTag.builder().instrumentTagName("piano2").build();
        InstrumentTag guitar = InstrumentTag.builder().instrumentTagName("guitar2").build();
        instrumentTagRepository.saveAll(Arrays.asList(piano, guitar));

        GenreTag pop = GenreTag.builder().genreTagName("pop2").build();
        GenreTag jazz = GenreTag.builder().genreTagName("jazz2").build();
        genreTagRepository.saveAll(Arrays.asList(pop, jazz));

        // When
//        soundManagerService.updateMusicTags(music.getMusicId(), request);

        // Then
        assertEquals(2, musicMoodTagRepository.findByIdMusicId(music.getMusicId()).size());
        assertEquals(2, musicInstrumentTagRepository.findByIdMusicId(music.getMusicId()).size());
        assertEquals(2, musicGenreTagRepository.findByIdMusicId(music.getMusicId()).size());
    }


}

package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired private SoundServiceImpl soundService;
    @Autowired private TagsServiceImpl tagsService;

    private Music music;

    @BeforeEach
    void insert(){
        //컨트롤러에서 넘어온 리퀘스트
        SoundSearchRequestDto request = SoundSearchRequestDto.builder()
                .mood(new ArrayList<>(Arrays.asList("sadservice", "sadtestservice")))                   // Mood 태그 추가
                .instrument(new ArrayList<>(Arrays.asList("snareservice", "snaretest2service")))  // Instrument 태그 추가
                .genre(new ArrayList<>(Arrays.asList("rockservice", "rocktestservice")))                   // Genre 태그 추가
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
                .moodTagName("sadservice")
                .build();
        moodTagRepository.save(moodTag);
        MoodTag moodTag2 = MoodTag.builder()
                .moodTagName("sadtestservice")
                .build();
        moodTagRepository.save(moodTag2);

        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("snareservice")
                .build();
        instrumentTagRepository.save(instrumentTag);
        InstrumentTag instrumentTag2 = InstrumentTag.builder()
                .instrumentTagName("snaretest2service")
                .build();
        instrumentTagRepository.save(instrumentTag2);

        GenreTag genreTag= GenreTag.builder()
                .genreTagName("rockservice")
                .build();
        genreTagRepository.save(genreTag);
        GenreTag genreTag2= GenreTag.builder()
                .genreTagName("rocktestservice")
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
    public void testReadAlbumByArtistName_WithNonExistingArtist() {
        // given
        String nickname = "nonExistingNickname";

        // when
        Optional<List<AlbumDto>> albums = soundService.readAlbumByArtistName(nickname);

        // then
        assertFalse(albums.isPresent(), "Albums should not be present for non-existing artist");
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

        // 저장된 music의 ID 확인
        int musicId = music.getMusicId();
        System.out.println("Saved Music ID: " + musicId);

        // Mood, Instrument, Genre 태그 생성
        MoodTag happy = MoodTag.builder().moodTagName("happy3").build();
        MoodTag energetic = MoodTag.builder().moodTagName("energetic3").build();
        moodTagRepository.saveAll(Arrays.asList(happy, energetic));

        InstrumentTag piano = InstrumentTag.builder().instrumentTagName("piano3").build();
        InstrumentTag guitar = InstrumentTag.builder().instrumentTagName("guitar3").build();
        instrumentTagRepository.saveAll(Arrays.asList(piano, guitar));

        GenreTag pop = GenreTag.builder().genreTagName("pop3").build();
        GenreTag jazz = GenreTag.builder().genreTagName("jazz3").build();
        genreTagRepository.saveAll(Arrays.asList(pop, jazz));

        // DTO 생성
        TagsDto tagsDto = new TagsDto();
        List<String> instrument = new ArrayList<>(List.of("piano3", "guitar3"));
        List<String> mood = new ArrayList<>(List.of("happy3", "energetic3"));
        List<String> genre = new ArrayList<>(List.of("pop3", "jazz3"));

        tagsDto.setInstrument(instrument);
        tagsDto.setMood(mood);
        tagsDto.setGenre(genre);

        // Act
        tagsService.updateSoundTags(musicId, tagsDto);

        // Then
        Music updatedMusic = musicRepository.findById(musicId)
                .orElseThrow(() -> new IllegalArgumentException("Music not found with ID: " + musicId));

        // Assertions can go here to verify the tags
        assertTrue(musicRepository.findById(updatedMusic.getMusicId()).isPresent());
    }

    @Test
    void testAlbumUpdate() {
        // 초기 AlbumDto 생성 및 저장
        AlbumDto dto = AlbumDto.builder()
                .userId(2)
                .albumName("Test_albu")
                .description("test album description234")
                .build();
        Album vo = dto.toEntity();
        Album album = albumRepository.save(vo);
        int albumId = album.getAlbumId();

        // 변경할 AlbumDto 생성
        AlbumDto changeDto = AlbumDto.builder()
                .userId(2)
                .albumName("Test_album_nobo")
                .description("test album description235")
                .build();

        // 업데이트 수행
        soundService.updateAlbum(albumId, changeDto);

        // 업데이트 결과 확인
        Album updatedAlbum = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Updated Album not found"));

        // 필드 값이 변경된 값과 일치하는지 검증
        assertEquals("Test_album_nobo", updatedAlbum.getAlbumName());
        assertEquals("test album description235", updatedAlbum.getDescription());
    }

    @Test
    void testUpdateMusic(){
        MusicDto musicDto = MusicDto.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();


        Music showLog = musicRepository.save(musicDto.toEntity());
        int musicId = showLog.getMusicId();

        MusicDto changeDto = MusicDto.builder()
                .soundType("sfx")
                .description("change")
                .title("change title")
                .build();

        soundService.updateMusic(musicId, changeDto);

        Music updatedMusic = musicRepository.findById(musicId).orElseThrow();
        assertEquals("sfx", updatedMusic.getSoundType());
        assertEquals("change", updatedMusic.getDescription());
        assertEquals("change title", updatedMusic.getTitle());
    }

}

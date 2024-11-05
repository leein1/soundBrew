package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.InstrumentTagDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.repository.sound.*;
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
class SoundAdminServiceTests {
    @Autowired private MusicRepository musicRepository;
    @Autowired private InstrumentTagRepository instrumentTagRepository;
    @Autowired private GenreTagRepository genreTagRepository;
    @Autowired private MoodTagRepository moodTagRepository;
    @Autowired private AlbumRepository albumRepository;

    @Autowired private MusicInstrumentTagRepository musicInstrumentTagRepository;
    @Autowired private MusicMoodTagRepository musicMoodTagRepository;
    @Autowired private MusicGenreTagRepository musicGenreTagRepository;

    @Autowired private SoundAdminService soundAdminService;
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

//    @Test
//    void changeSpelling() {
//        String before = "rack";
//
//        GenreTag genreTag3 = GenreTag.builder()
//                .genreTagName("rack")
//                .build();
//        genreTagRepository.save(genreTag3);
//
//        SoundSearchRequestDto dto = new SoundSearchRequestDto();
//        dto.setGenre(List.of("chagetestest"));
//        soundAdminService.updateTagSpelling(before,dto);
//
//        assertEquals("chagetestest", dto.getGenre().get(0));
//    }

    @Test
    void testDeleteAlbum() {
        // Given
        Album album = Album.builder()
                .userId(2)
                .albumName("Test_album_no.1")
                .albumArtPath("/test/test/path")
                .description("test album description")
                .build();
        albumRepository.save(album);

        // When
        soundAdminService.deleteAlbum(album.getAlbumId());

        // Then
        assertTrue(albumRepository.findById(album.getAlbumId()).isEmpty());
    }

    @Test
    void testDeleteMusic() {
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

        // When
        soundAdminService.deleteMusic(music.getMusicId());

        // Then
        assertTrue(musicRepository.findById(music.getMusicId()).isEmpty());
    }

    @Test
    void testUpdateInstTagSpelling(){
        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("snaretest")
                .build();
        instrumentTagRepository.save(instrumentTag);
        assertEquals("snaretest",instrumentTag.getInstrumentTagName());

        soundAdminService.updateInstrumentTagSpelling("snaretest", "snarechange");
        assertEquals("snarechange",instrumentTag.getInstrumentTagName());
    }
    @Test
    void testUpdateMoodTagSpelling(){
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("specialsad")
                .build();
        moodTagRepository.save(moodTag);
        assertEquals("specialsad", moodTag.getMoodTagName());

        soundAdminService.updateMoodTagSpelling("specialsad","specialchange");
        assertEquals("specialchange", moodTag.getMoodTagName());
    }
    @Test
    void testUpdateGenreTagSpelling(){
        GenreTag genreTag = GenreTag.builder()
                .genreTagName("dreampop")
                .build();
        genreTagRepository.save(genreTag);
        assertEquals("dreampop", genreTag.getGenreTagName());

        soundAdminService.updateGenreTagSpelling("dreampop", "dreamrnb");
        assertEquals("dreamrnb", genreTag.getGenreTagName());
    }

    @Test
    void testInstCreateTest(){
        InstrumentTagDto instrumentTagDto= new InstrumentTagDto();
        instrumentTagDto.setInstrument(List.of("specialviolin"));

        soundAdminService.createInstTag(instrumentTagDto);

        assertTrue(instrumentTagRepository.findByInstrumentTagName("specialviolin").isPresent());
    }
    @Test
    void testMoodCreateTest(){
        MoodTagDto moodTag = new MoodTagDto();
        moodTag.setMood(List.of("specialhappy"));

        soundAdminService.createMoodTag(moodTag);
        assertTrue(moodTagRepository.findByMoodTagName("specialhappy").isPresent());
    }
    @Test
    void testGenreCreateTest(){
        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(List.of("specialPop"));

        soundAdminService.createGenreTag(genreTagDto);
        assertTrue(genreTagRepository.findByGenreTagName("specialPop").isPresent());
    }

}
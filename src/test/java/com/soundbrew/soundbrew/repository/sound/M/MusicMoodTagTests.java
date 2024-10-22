package com.soundbrew.soundbrew.repository.sound.M;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTag;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTagId;
import com.soundbrew.soundbrew.repository.sound.MoodTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicMoodTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Log4j2
public class MusicMoodTagTests {
    @Autowired
    private MusicMoodTagRepository musicMoodTagRepository;

    @Autowired
    private MoodTagRepository moodTagRepository;

    @Autowired
    private MusicRepository musicRepository;

    private MusicMoodTagId musicMoodTagId;

    @Test
    @Transactional
    void insert(){
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();

        moodTagRepository.save(moodTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicMoodTagId = MusicMoodTagId.builder()
                .musicId(music.getMusicId())
                .moodTagId(moodTag.getMoodTagId())
                .build();

        MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                    .id(musicMoodTagId)
                    .music(music)
                    .moodTag(moodTag)
                    .build();

        musicMoodTagRepository.save(musicMoodTag);

        log.info("=======");
        log.info(musicMoodTag.toString());
        log.info("=======");
    }

    @Test
    @Transactional
    void delete(){
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();

        moodTagRepository.save(moodTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicMoodTagId = MusicMoodTagId.builder()
                .musicId(music.getMusicId())
                .moodTagId(moodTag.getMoodTagId())
                .build();

        MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                .id(musicMoodTagId)
                .music(music)
                .moodTag(moodTag)
                .build();

        musicMoodTagRepository.save(musicMoodTag);

        musicMoodTagRepository.deleteById(musicMoodTagId);
        assertFalse(musicMoodTagRepository.existsById(musicMoodTagId));
    }

    @Test
    @Transactional
    void List(){
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();

        moodTagRepository.save(moodTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicMoodTagId = MusicMoodTagId.builder()
                .musicId(music.getMusicId())
                .moodTagId(moodTag.getMoodTagId())
                .build();

        MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                .id(musicMoodTagId)
                .music(music)
                .moodTag(moodTag)
                .build();

        musicMoodTagRepository.save(musicMoodTag);

        assertNotNull(musicMoodTagRepository.findById(musicMoodTagId));
    }

    @Test
    @Transactional
    void music(){
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();

        moodTagRepository.save(moodTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicMoodTagId = MusicMoodTagId.builder()
                .musicId(music.getMusicId())
                .moodTagId(moodTag.getMoodTagId())
                .build();

        MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                .id(musicMoodTagId)
                .music(music)
                .moodTag(moodTag)
                .build();

        musicMoodTagRepository.save(musicMoodTag);

        List<MusicMoodTag> result = musicMoodTagRepository.findByIdMusicId(1);

        result.forEach(value -> value.getId());
    }

    @Test
    @Transactional
    void tagId(){
        MoodTag moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();

        moodTagRepository.save(moodTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicMoodTagId = MusicMoodTagId.builder()
                .musicId(music.getMusicId())
                .moodTagId(moodTag.getMoodTagId())
                .build();

        MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                .id(musicMoodTagId)
                .music(music)
                .moodTag(moodTag)
                .build();

        musicMoodTagRepository.save(musicMoodTag);


        List<MusicMoodTag> result = musicMoodTagRepository.findByIdMoodTagId(1);
        result.forEach(value -> value.getId());
    }
}

package com.soundbrew.soundbrew.repository.sound.M;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.domain.sound.MusicGenreTag;
import com.soundbrew.soundbrew.domain.sound.MusicGenreTagId;
import com.soundbrew.soundbrew.repository.sound.GenreTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicGenreTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class MusicGenreTagTests {

    @Autowired
    private MusicGenreTagRepository musicGenreTagRepository;

    @Autowired
    private GenreTagRepository genreTagRepository;

    @Autowired
    private MusicRepository musicRepository;
    private MusicGenreTagId musicGenreTagId;

    @Test
    @Transactional
    void insert() {
        // 미리 값을 생성하고 저장
        GenreTag genreTag = GenreTag.builder()
                .genreTagName("ponk")
                .build();
        genreTagRepository.save(genreTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        // 복합키 생성
        musicGenreTagId = MusicGenreTagId.builder()
                .musicId(music.getMusicId())
                .genreTagId(genreTag.getGenreTagId())
                .build();

        MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                .id(musicGenreTagId)
                .music(music)
                .genreTag(genreTag)
                .build();

        musicGenreTagRepository.save(musicGenreTag);

        log.info("=======");
        log.info(musicGenreTag.toString());
        log.info("=======");
    }

    @Test
    @Transactional
    void delete(){
        // 미리 값을 생성하고 저장
        GenreTag genreTag = GenreTag.builder()
                .genreTagName("ponk")
                .build();
        genreTagRepository.save(genreTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        // 복합키 생성
        musicGenreTagId = MusicGenreTagId.builder()
                .musicId(music.getMusicId())
                .genreTagId(genreTag.getGenreTagId())
                .build();

        MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                .id(musicGenreTagId)
                .music(music)
                .genreTag(genreTag)
                .build();

        musicGenreTagRepository.save(musicGenreTag);

        log.info("=======");
        log.info(musicGenreTag.toString());
        log.info("=======");

        musicGenreTagRepository.deleteById(musicGenreTagId);
        assertFalse(musicGenreTagRepository.existsById(musicGenreTagId));

    }

    @Test
    @Transactional
    void selectOne(){
        // 미리 값을 생성하고 저장
        GenreTag genreTag = GenreTag.builder()
                .genreTagName("ponk")
                .build();
        genreTagRepository.save(genreTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        // 복합키 생성
        musicGenreTagId = MusicGenreTagId.builder()
                .musicId(music.getMusicId())
                .genreTagId(genreTag.getGenreTagId())
                .build();

        MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                .id(musicGenreTagId)
                .music(music)
                .genreTag(genreTag)
                .build();

        musicGenreTagRepository.save(musicGenreTag);

        MusicGenreTag result = musicGenreTagRepository.findById(musicGenreTagId).orElseThrow();

        assertNotNull(result);
    }

    @Test
    @Transactional
    void searchByMusicId(){
        // 미리 값을 생성하고 저장
        GenreTag genreTag = GenreTag.builder()
                .genreTagName("ponk")
                .build();
        genreTagRepository.save(genreTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        // 복합키 생성
        musicGenreTagId = MusicGenreTagId.builder()
                .musicId(music.getMusicId())
                .genreTagId(genreTag.getGenreTagId()) // musicGenreTagId 대신 genreTagId
                .build();

        MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                .id(musicGenreTagId)
                .music(music)
                .genreTag(genreTag)
                .build();

        musicGenreTagRepository.save(musicGenreTag);

        List<MusicGenreTag> resultList = musicGenreTagRepository.findByIdMusicId(1);

        resultList.forEach(result -> result.getId());
    }
//
    @Test
    @Transactional
    void searchByGenreId(){
        // 미리 값을 생성하고 저장
        GenreTag genreTag = GenreTag.builder()
                .genreTagName("ponk")
                .build();
        genreTagRepository.save(genreTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        // 복합키 생성
        musicGenreTagId = MusicGenreTagId.builder()
                .musicId(music.getMusicId())
                .genreTagId(genreTag.getGenreTagId())
                .build();

        MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                .id(musicGenreTagId)
                .music(music)
                .genreTag(genreTag)
                .build();

        musicGenreTagRepository.save(musicGenreTag);

        List<MusicGenreTag> resultList = musicGenreTagRepository.findByIdGenreTagId(1);

        resultList.forEach(result -> result.getId());
    }




}

package com.soundbrew.soundbrew.repository.sound.N;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import com.soundbrew.soundbrew.repository.sound.GenreTagRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Log4j2
public class GenreTagRepositoryTests {
    @Autowired
    private GenreTagRepository genreTagRepository;

    private GenreTag genreTag;

    @BeforeEach
    void create(){
        genreTag= GenreTag.builder()
                .genreTagName("rock")
                .build();

        genreTag = genreTagRepository.save(genreTag);
        assertEquals("rock", genreTag.getGenreTagName());
    }

    @AfterEach
    void delete(){
        genreTagRepository.deleteById(genreTag.getGenreTagId());

        assertFalse(genreTagRepository.existsById(genreTag.getGenreTagId()));
    }

    @Test
    @Transactional
    void name(){
        GenreTag result = genreTagRepository.findByGenreTagName("rock").orElseThrow();
        assertEquals("rock", result.getGenreTagName());
    }


    @Test
    @Transactional
    void read(){
        genreTagRepository.findById(genreTag.getGenreTagId());

        assertEquals("rock",genreTag.getGenreTagName());
    }

    @Test
    @Transactional
    void update(){
        GenreTag modify = genreTagRepository.findById(genreTag.getGenreTagId()).orElseThrow();
        genreTag.update("popsong");
        assertEquals("popsong",modify.getGenreTagName());
    }

}

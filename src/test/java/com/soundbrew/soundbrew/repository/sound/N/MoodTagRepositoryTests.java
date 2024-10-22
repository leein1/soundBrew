package com.soundbrew.soundbrew.repository.sound.N;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import com.soundbrew.soundbrew.repository.sound.MoodTagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class MoodTagRepositoryTests {
    @Autowired
    private MoodTagRepository moodTagRepository;

    private MoodTag moodTag;
    @BeforeEach
    void create(){
        moodTag = MoodTag.builder()
                .moodTagName("sad")
                .build();

        moodTagRepository.save(moodTag);
        assertEquals("sad",moodTag.getMoodTagName());
    }

    @AfterEach
    void delete(){
        moodTagRepository.deleteById(moodTag.getMoodTagId());

        assertFalse(moodTagRepository.existsById(moodTag.getMoodTagId()));
    }


    @Test
    @Transactional
    void read(){
        moodTagRepository.findById(moodTag.getMoodTagId());
        assertEquals("sad",moodTag.getMoodTagName());
    }

    @Test
    @Transactional
    void update(){
        MoodTag modify = moodTagRepository.findById(moodTag.getMoodTagId()).orElseThrow();
        modify.update("supersad");
        assertEquals("supersad",moodTag.getMoodTagName());
    }
}

package com.soundbrew.soundbrew.repository.sound.N;

import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import com.soundbrew.soundbrew.repository.sound.InstrumentTagRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Log4j2
@SpringBootTest
public class TagsDtoRepositoryTests {
    @Autowired
    private InstrumentTagRepository instrumentTagRepository;

    private InstrumentTag instrumentTag;

    @BeforeEach
    void create(){
        instrumentTag = InstrumentTag.builder()
                        .instrumentTagName("snare")
                        .build();
        instrumentTagRepository.save(instrumentTag);
        assertEquals("snare",instrumentTag.getInstrumentTagName());
    }

    @AfterEach
    void delete(){
        instrumentTagRepository.deleteById(instrumentTag.getInstrumentTagId());

        assertFalse(instrumentTagRepository.existsById(instrumentTag.getInstrumentTagId()));
    }

    @Test
    @Transactional
    void name(){
        InstrumentTag result = instrumentTagRepository.findByInstrumentTagName("snare").orElseThrow();

        assertEquals("snare", result.getInstrumentTagName());
    }


    @Test
    @Transactional
    void read(){
        instrumentTagRepository.findById(instrumentTag.getInstrumentTagId());
        assertEquals("snare",instrumentTag.getInstrumentTagName());
    }

    @Test
    @Transactional
    void update(){
        InstrumentTag modify = instrumentTagRepository.findById(instrumentTag.getInstrumentTagId()).orElseThrow();
        modify.update("tom");
        assertEquals("tom", modify.getInstrumentTagName());
    }
}

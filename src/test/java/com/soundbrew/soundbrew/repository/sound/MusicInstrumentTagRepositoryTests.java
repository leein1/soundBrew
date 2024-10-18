package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTagId;
import com.soundbrew.soundbrew.dto.MusicInstrumentTagDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Log4j2
public class MusicInstrumentTagRepositoryTests {

    @Autowired
    private MusicInstrumentTagRepository musicInstrumentTagRepository;


    @Test
    @Transactional
    void testFindById(){
        MusicInstrumentTagId id = MusicInstrumentTagId.builder()
                .music_id(1)
                .instrument_tag_id(1)
                .build();

        MusicInstrumentTag search = musicInstrumentTagRepository.findById(id).orElse(null);

        log.info("=====");
        log.info(search);
        log.info("=====");


    }

    @Test
    void testSearch(){
        List<MusicInstrumentTagDto> findAllMusicAndTag =musicInstrumentTagRepository.findAllMusicAndTag();

        log.info("=======");
        log.info(findAllMusicAndTag);
        log.info("=======");

    }

}

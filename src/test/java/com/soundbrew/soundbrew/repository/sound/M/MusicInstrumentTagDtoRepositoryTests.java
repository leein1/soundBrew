package com.soundbrew.soundbrew.repository.sound.M;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.repository.sound.InstrumentTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicInstrumentTagRepository;
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
public class MusicInstrumentTagDtoRepositoryTests {

    @Autowired
    private MusicInstrumentTagRepository musicInstrumentTagRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private InstrumentTagRepository instrumentTagRepository;

    private MusicInstrumentTagId musicInstrumentTagId;

    @Test
    @Transactional
    void insert() {
        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("viola")
                .build();
        instrumentTagRepository.save(instrumentTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicInstrumentTagId = MusicInstrumentTagId.builder()
                .musicId(music.getMusicId())
                .instrumentTagId(instrumentTag.getInstrumentTagId())
                .build();

        MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                .id(musicInstrumentTagId)
                .music(music)
                .instrumentTag(instrumentTag)
                .build();
        musicInstrumentTagRepository.save(musicInstrumentTag);

        log.info("=======");
        log.info(musicInstrumentTag.toString());
        log.info("=======");
    }

    @Transactional
    @Test
    void deleteByMusicId() {
        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("viola")
                .build();
        instrumentTagRepository.save(instrumentTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        MusicInstrumentTagId musicInstrumentTagId = MusicInstrumentTagId.builder()
                .musicId(music.getMusicId())
                .instrumentTagId(instrumentTag.getInstrumentTagId())
                .build();

        MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                .id(musicInstrumentTagId)
                .music(music)
                .instrumentTag(instrumentTag)
                .build();
        musicInstrumentTagRepository.save(musicInstrumentTag);

        // musicId로 연결된 태그들 삭제
        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());

        // 삭제 후 태그가 존재하지 않는지 확인
        assertFalse(musicInstrumentTagRepository.existsById(musicInstrumentTagId));
    }

    @Transactional
    @Test
    void delete(){
        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("viola")
                .build();
        instrumentTagRepository.save(instrumentTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicInstrumentTagId = MusicInstrumentTagId.builder()
                .musicId(music.getMusicId())
                .instrumentTagId(instrumentTag.getInstrumentTagId())
                .build();

        MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                .id(musicInstrumentTagId)
                .music(music)
                .instrumentTag(instrumentTag)
                .build();
        musicInstrumentTagRepository.save(musicInstrumentTag);

        musicInstrumentTagRepository.deleteById(musicInstrumentTagId);
        assertFalse(musicInstrumentTagRepository.existsById(musicInstrumentTagId));
    }

    @Test
    @Transactional
    void List(){

        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("viola")
                .build();
        instrumentTagRepository.save(instrumentTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicInstrumentTagId = MusicInstrumentTagId.builder()
                .musicId(music.getMusicId())
                .instrumentTagId(instrumentTag.getInstrumentTagId())
                .build();

        MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                .id(musicInstrumentTagId)
                .music(music)
                .instrumentTag(instrumentTag)
                .build();
        musicInstrumentTagRepository.save(musicInstrumentTag);

        assertNotNull(musicInstrumentTagRepository.findById(musicInstrumentTagId));
    }

    @Test
    @Transactional
    void musicId(){
        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("viola")
                .build();
        instrumentTagRepository.save(instrumentTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicInstrumentTagId = MusicInstrumentTagId.builder()
                .musicId(music.getMusicId())
                .instrumentTagId(instrumentTag.getInstrumentTagId())
                .build();

        MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                .id(musicInstrumentTagId)
                .music(music)
                .instrumentTag(instrumentTag)
                .build();
        musicInstrumentTagRepository.save(musicInstrumentTag);

        List<MusicInstrumentTag> result = musicInstrumentTagRepository.findByIdMusicId(1);
        result.forEach(value -> value.getId());
    }

    @Test
    @Transactional
    void instrumentId(){
        InstrumentTag instrumentTag = InstrumentTag.builder()
                .instrumentTagName("viola")
                .build();
        instrumentTagRepository.save(instrumentTag);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        musicInstrumentTagId = MusicInstrumentTagId.builder()
                .musicId(music.getMusicId())
                .instrumentTagId(instrumentTag.getInstrumentTagId())
                .build();

        MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                .id(musicInstrumentTagId)
                .music(music)
                .instrumentTag(instrumentTag)
                .build();
        musicInstrumentTagRepository.save(musicInstrumentTag);

        List<MusicInstrumentTag> result = musicInstrumentTagRepository.findByIdInstrumentTagId(1);
        result.forEach(value -> value.getId());
    }

}

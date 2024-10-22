package com.soundbrew.soundbrew.repository.sound.N;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class MusicRepositoryTests {

    @Autowired
    private MusicRepository musicRepository;

    // create
    @Test
    void testInsert(){
        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();

        Music showLog = musicRepository.save(music);

        log.info(showLog);
    }

    // read
    @Test
    void testSearch(){
        int musicId = 1;

        Music showLog = musicRepository.findById(musicId).orElse(null);
        log.info(showLog);
    }

    @Test
    @Transactional
    void testModify(){
        int musicId = 1;

        Music beforeModify = musicRepository.findById(musicId).orElse(null);
        beforeModify.update("수정된 타이틀 확인", "수정된 곡 설명 확인", beforeModify.getSoundType());

        // 변경된 내용이 실제로 반영되었는지 확인
        assertEquals("수정된 타이틀 확인", beforeModify.getTitle());
        assertEquals("수정된 곡 설명 확인", beforeModify.getDescription());

        log.info(beforeModify.toString());
    }

    @Test
    @Transactional
    void testDelete(){
        int musicId = 1;

        // 삭제 전, 해당 엔티티가 존재하는지 확인
        assertTrue(musicRepository.findById(musicId).isPresent());
        musicRepository.deleteById(musicId);

        // 삭제 후, 해당 엔티티가 더 이상 존재하지 않는지 확인
        assertFalse(musicRepository.findById(musicId).isPresent());
    }





}

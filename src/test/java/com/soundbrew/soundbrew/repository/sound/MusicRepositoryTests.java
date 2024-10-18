package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class MusicRepositoryTests {

    @Autowired
    private MusicRepository musicRepository;

    @Test
    void testInsert(){

            Music music = Music.builder()
                    .title("fury")
                    .file_path("/file/test/music_path_test_hello_fury")
                    .price(3)
                    .description("Jonsi의 fury 팔세토가 돋보입니다.")
                    .user_id(2)
                    .sound_type("music")
                    .build();

            Music showLog = musicRepository.save(music);

            log.info(showLog);

    }

    @Test
    void testSearch(){

    }

}

package com.soundbrew.soundbrew.repository.AlbumAndMusic;

import com.soundbrew.soundbrew.domain.Music;
import com.soundbrew.soundbrew.repository.MusicRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MusicRepositoryTests {

    @Autowired
    private MusicRepository musicRepository;

    @Test
    void testInsert(){
        IntStream.rangeClosed(1,10).forEach(a -> {
            Music music = Music.builder()
                    .title("Tornado"+a)
                    .file_path("/file/test/music_path_test_"+a)
                    .price(3)
                    .description("Jonsi의 풍성한 곡 편성이 돋보이는 곡 입니다. 갈수록 곡이 풍성하고 격정적입니다.")
                    .user_id(2)
                    .build();

            Music showLog = musicRepository.save(music);

            log.info(showLog);
        });
    }

    @Test
    void testSearch(){

    }

}

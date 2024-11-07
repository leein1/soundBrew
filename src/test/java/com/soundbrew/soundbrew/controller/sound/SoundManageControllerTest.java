package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SoundManageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private SoundManagerService soundManagerService;

    @Test
    void createSound() throws Exception {
        AlbumDto albumDto = AlbumDto.builder()
                .userId(2)
                .albumName("Test_album_no.1")
                .albumArtPath("/test/test/path")
                .description("test album description")
                .build();

        MusicDto musicDto = MusicDto.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();

        InstrumentTagDto instrumentTagDto = new InstrumentTagDto();
        instrumentTagDto.setInstrument(List.of("guitar","piano"));
        MoodTagDto moodTagDto = new MoodTagDto();
        moodTagDto.setMood(List.of("happy","sad"));
        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(List.of("rock"));

        mockMvc.perform(post("/manage/sounds")
                        .flashAttr("albumDto", albumDto)
                        .flashAttr("musicDto",musicDto)
                        .flashAttr("instrumentTagDto",instrumentTagDto)
                        .flashAttr("moodTagDto",moodTagDto)
                        .flashAttr("genreTagDto",genreTagDto)
                )
                .andDo(print())
                .andExpect(status().isOk()) // 예상되는 상태 코드
                .andExpect(view().name("empty"));
    }

    @Test
    void updateAlbum() throws Exception {
        AlbumDto albumDto = AlbumDto.builder()
                .albumName("Updated Album Name")
                .description("Updated album description")
                .build();

        mockMvc.perform(patch("/manage/albums/11")
                        .flashAttr("albumId", 11)
                        .flashAttr("albumDto", albumDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
// 테스트 후 데이터가 롤백되도록 설정
    void updateMusic() throws Exception {
        // 업데이트할 데이터 준비
        MusicDto musicDto = MusicDto.builder()
                .title("Updated Music Title")
                .description("Updated description of the music")
                .soundType("sfx")
                .build();

        // MockMvc로 PATCH 요청
        mockMvc.perform(patch("/manage/musics/" + 123)
                        .flashAttr("musicDto", musicDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void updateMusicTags() throws Exception {
        InstrumentTagDto instrumentTagDto = new InstrumentTagDto();
        instrumentTagDto.setInstrument(List.of("newGuitar", "newPiano"));

        MoodTagDto moodTagDto = new MoodTagDto();
        moodTagDto.setMood(List.of("energetic", "calm"));

        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(List.of("electronic", "jazz"));

        mockMvc.perform(post("/manage/musics/1/tags")
                        .flashAttr("musicId", 1)
                        .flashAttr("instrumentTagDto", instrumentTagDto)
                        .flashAttr("moodTagDto", moodTagDto)
                        .flashAttr("genreTagDto", genreTagDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }
}
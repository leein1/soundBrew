package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchFilterDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.service.sound.SoundSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class SoundSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SoundSearchService soundSearchService;

    @Test
    void getMusicList() throws Exception {
        SoundSearchRequestDto requestDto = new SoundSearchRequestDto();
        requestDto.setNickname("u_1");

        mockMvc.perform(get("/sounds")
                        .flashAttr("soundSearchRequestDto", requestDto)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("sound/music-list"));
    }


    @Test
    void getAlbumOne() throws Exception {
        int albumId = 11;
        mockMvc.perform(get("/sounds/albums/{albumId}",albumId)
                        )
                .andExpect(status().isOk())
                .andExpect(view().name("sound/album-list"));

    }

    @Test
    void getArtistOne() throws Exception {
        String nickname = "u_1";
        mockMvc.perform(get("/sounds/artists/{nickname}",nickname))
                .andExpect(status().isOk());
    }


    @Test
    void getSoundOne() throws Exception {
        int musicId = 11;
        mockMvc.perform(get("/sounds/musics/{musicId}",musicId))
                .andExpect(status().isOk());
    }
}
package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.InstrumentTagDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.service.sound.SoundAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class SoundAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoundAdminService soundAdminService;

    @Test
    void deleteAlbum() throws Exception {
        mockMvc.perform(delete("/admin/albums/"+11)
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void deleteMusic() throws Exception {
        mockMvc.perform(delete("/admin/musics/251")
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void changeInstSpelling() throws Exception {
        mockMvc.perform(patch("/admin/tags/instruments")
                        .flashAttr("beforeName", "oldInstrument")
                        .flashAttr("afterName", "newInstrument"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
        verify(soundAdminService, times(1)).updateInstrumentTagSpelling("oldInstrument", "newInstrument");
    }

    @Test
    void changeMoodSpelling() throws Exception {
        mockMvc.perform(patch("/admin/tags/moods")
                        .flashAttr("beforeName", "oldMood")
                        .flashAttr("afterName", "newMood"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void changeGenreSpelling() throws Exception {
        mockMvc.perform(patch("/admin/tags/genres")
                        .flashAttr("beforeName", "oldGenre")
                        .flashAttr("afterName", "newGenre"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void makeInstTag() throws Exception {
        InstrumentTagDto instrumentTagDto = new InstrumentTagDto();
        instrumentTagDto.setInstrument(List.of("newInstrument"));

        mockMvc.perform(post("/admin/tags/instruments")
                        .flashAttr("instrumentTagDto", instrumentTagDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void makeMoodTag() throws Exception {
        MoodTagDto moodTagDto = new MoodTagDto();
        moodTagDto.setMood(List.of("newMood"));

        mockMvc.perform(post("/admin/tags/moods")
                        .flashAttr("moodTagDto", moodTagDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void makeGenreTag() throws Exception {
        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(List.of("newGenre"));

        mockMvc.perform(post("/admin/tags/genres")
                        .flashAttr("genreTagDto", genreTagDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }
}
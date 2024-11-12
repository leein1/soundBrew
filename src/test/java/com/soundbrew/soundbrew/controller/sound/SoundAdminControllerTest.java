package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import com.soundbrew.soundbrew.service.sound.TagsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SoundAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagsServiceImpl soundAdminService;

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
        mockMvc.perform(delete("/admin/musics/277")
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void changeInstSpelling() throws Exception {
        mockMvc.perform(patch("/admin/tags/instruments")
                        .flashAttr("beforeName", "newInstrument")
                        .flashAttr("afterName", "newrealInstrument"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
        verify(soundAdminService, times(1)).updateInstrumentTagSpelling("newInstrument", "newrealInstrument");
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
        TagsDto tagsDto = new TagsDto();
        tagsDto.setInstrument(List.of("newreadInstrument"));

        mockMvc.perform(post("/admin/tags/instruments")
                        .flashAttr("tagsDto", tagsDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void makeMoodTag() throws Exception {
        TagsDto tagsDto = new TagsDto();
        tagsDto.setMood(List.of("newMood"));

        mockMvc.perform(post("/admin/tags/moods")
                        .flashAttr("tagsDto", tagsDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }

    @Test
    void makeGenreTag() throws Exception {
        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(List.of("newGenre"));

        mockMvc.perform(post("/admin/tags/genres")
                        .flashAttr("tagsDto", genreTagDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("empty"));
    }
}
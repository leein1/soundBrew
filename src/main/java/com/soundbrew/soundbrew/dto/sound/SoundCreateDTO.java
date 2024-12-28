package com.soundbrew.soundbrew.dto.sound;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SoundCreateDTO {
    private AlbumDTO albumDTO;
    private MusicDTO musicDTO;
    private TagsDTO tagsDTO;
}

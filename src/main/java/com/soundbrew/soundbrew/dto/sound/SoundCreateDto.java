package com.soundbrew.soundbrew.dto.sound;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SoundCreateDto {
    private AlbumDto albumDto;
    private MusicDto musicDto;
    private TagsDto tagsDto;

}

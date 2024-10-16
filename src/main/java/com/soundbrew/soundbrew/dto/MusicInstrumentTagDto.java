package com.soundbrew.soundbrew.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class MusicInstrumentTagDto {
    private int music_id;
    private String title;
    private String description;
    private int instrument_tag_id;
    private String instrument_tag_name;
}

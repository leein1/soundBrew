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
    private int musicId;
    private String title;
    private String description;
    private int instrumentTagId;
    private String instrumentTagName;
}

package com.soundbrew.soundbrew.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
public class SoundSearchResultDto {

    private int albumId;
    private String albumName;
    private String albumArtPath;
    private String albumDescription;
    private int musicId;
    private String musicTitle;
    private String musicFilePath;
    private int price;
    private String musicDescription;
    private String userName;
    private String instrumentTagName;
    private String moodTagName;
    private String genreTagName;



}

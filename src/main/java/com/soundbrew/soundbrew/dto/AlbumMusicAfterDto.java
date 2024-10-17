package com.soundbrew.soundbrew.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AlbumMusicAfterDto{

    private int album_id;
    private String album_name;
    private String album_art_path;
    private String album_description;
    private int music_id;
    private String music_title;
    private String music_file_path;
    private int price;
    private String music_description;
    private String user_name;
    private List<String> instrument_tag_name;
    private List<String> mood_tag_name;
    private List<String> genre_tag_name;

}

package com.soundbrew.soundbrew.dto.sound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
public class SearchAlbumResultDto {
    private int albumId;
    private String albumName;
    private String albumArtPath;
    private String albumDescription;
    private String userName;
    private String instrumentTagName;
    private String moodTagName;
    private String genreTagName;
    private LocalDateTime create_date;
    private LocalDateTime modify_date;
}

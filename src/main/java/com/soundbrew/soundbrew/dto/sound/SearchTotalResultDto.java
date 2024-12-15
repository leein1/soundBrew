package com.soundbrew.soundbrew.dto.sound;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SearchTotalResultDto {

    private int albumId;
    private String albumName;
    private String albumArtPath;
    private String albumDescription;
    private int musicId;
    private String musicTitle;
    private String musicFilePath;
    private int price;
    private String musicDescription;
    private String nickname;
    private String instrumentTagName;
    private String moodTagName;
    private String genreTagName;
    private LocalDateTime create_date;
    private LocalDateTime modify_date;

    public SearchTotalResultDto(int albumId, String albumName, String albumArtPath, String albumDescription,
                                String nickname, String instrumentTagName, String moodTagName, String genreTagName,
                                LocalDateTime create_date, LocalDateTime modify_date) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumArtPath = albumArtPath;
        this.albumDescription = albumDescription;
        this.nickname = nickname;
        this.instrumentTagName = instrumentTagName;
        this.moodTagName = moodTagName;
        this.genreTagName = genreTagName;
        this.create_date = create_date;
        this.modify_date = modify_date;
    }

    public SearchTotalResultDto(
                                int musicId, String musicTitle, String musicFilePath, int price, String musicDescription,
                                String nickname, String instrumentTagName, String moodTagName, String genreTagName,
                                LocalDateTime create_date, LocalDateTime modify_date) {
        this.musicId = musicId;
        this.musicTitle = musicTitle;
        this.musicFilePath = musicFilePath;
        this.price = price;
        this.musicDescription = musicDescription;
        this.nickname = nickname;
        this.instrumentTagName = instrumentTagName;
        this.moodTagName = moodTagName;
        this.genreTagName = genreTagName;
        this.create_date = create_date;
        this.modify_date = modify_date;
    }

}

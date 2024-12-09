package com.soundbrew.soundbrew.dto.sound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
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


    private String testField;

    public SearchTotalResultDto(int albumId, String albumName, String albumArtPath, String albumDescription, int musicId,
                                String musicTitle, String musicFilePath, int price, String musicDescription,
                                String nickname, String instrumentTagName, String moodTagName, String genreTagName,
                                LocalDateTime create_date, LocalDateTime modify_date) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumArtPath = albumArtPath;
        this.albumDescription = albumDescription;
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
        // testField는 쿼리 결과와 매핑되지 않으므로 초기화되지 않습니다.
    }

}

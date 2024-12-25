package com.soundbrew.soundbrew.dto.sound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.soundbrew.soundbrew.dto.BaseEntityDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SearchTotalResultDto extends BaseEntityDto {
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

}

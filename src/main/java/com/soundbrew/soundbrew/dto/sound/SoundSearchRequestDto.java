package com.soundbrew.soundbrew.dto.sound;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Setter
public class SoundSearchRequestDto {
    private String nickname;
    private Integer musicId;
    private Integer albumId;
    private List<String> instrument;
    private List<String> mood;
    private List<String> genre;

    private String keyword;
    //태그, 검색어 조건 넣기

}

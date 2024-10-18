package com.soundbrew.soundbrew.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class SoundReqeustDto {
    private String nickname;
    private Integer musicId;
    private Integer albumId;
    private List<String> instTags;
    private List<String> moodTags;
    private List<String> genreTags;
    private Pageable pageable;

}

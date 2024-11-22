package com.soundbrew.soundbrew.dto.sound;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MusicTagsDto {
    private Integer musicId;
    private String title;
    private List<String> instrumentTags;
    private List<String> genreTags;
    private List<String> moodTags;
}

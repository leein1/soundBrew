package com.soundbrew.soundbrew.dto.sound;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
public class SoundSearchFilterDto {
    private List<SoundSearchResultDto> soundSearchResultDto;
    private Set<String> instTag;
    private Set<String> moodTag;
    private Set<String> genreTag;
}

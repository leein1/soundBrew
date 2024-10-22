package com.soundbrew.soundbrew.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
public class SoundServiceDto {
    private List<SoundRepositoryDto> soundRepositoryDto;
    private Set<String> instTag;
    private Set<String> moodTag;
    private Set<String> genreTag;
}

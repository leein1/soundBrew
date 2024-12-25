package com.soundbrew.soundbrew.dto.sound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TagsStreamDTO {
    private String instrumentTagName;
    private String moodTagName;
    private String genreTagName;
}

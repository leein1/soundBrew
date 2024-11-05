package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MoodTagDto {
    private List<String> mood;

    public List<MoodTag> toEntity(){
        return mood.stream()
                .map(tagName -> MoodTag.builder()
                        .moodTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }
}

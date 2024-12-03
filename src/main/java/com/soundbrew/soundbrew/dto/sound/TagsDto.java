package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MoodTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.security.DenyAll;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TagsDto {
    private Integer musicId;
    private String title;
    private List<String> instrument;
    private List<String> mood;
    private List<String> genre;

    public List<InstrumentTag> InstToEntity(){
        return instrument.stream()
                .map(tagName -> InstrumentTag.builder()
                        .instrumentTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }

    public List<MoodTag> moodToEntity(){
        return mood.stream()
                .map(tagName -> MoodTag.builder()
                        .moodTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }

    public List<GenreTag> genreToEntity(){
        return genre.stream()
                .map(tagName -> GenreTag.builder()
                        .genreTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }

}

package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GenreTagDto {
    private List<String> genre;

    public List<GenreTag> toEntityList() {
        return genre.stream()
                .map(tagName -> GenreTag.builder()
                        .genreTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }
}

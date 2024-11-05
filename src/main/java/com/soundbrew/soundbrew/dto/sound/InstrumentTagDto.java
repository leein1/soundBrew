package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class InstrumentTagDto {
    private List<String> instrument;

    public List<InstrumentTag> toEntity(){
        return instrument.stream()
                .map(tagName -> InstrumentTag.builder()
                        .instrumentTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }
}


package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MoodTag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagNameDto {
    private String beforeName;
    private String afterName;

    public InstrumentTag toInstEntity(){
        return InstrumentTag.builder()
                .instrumentTagName(this.afterName)
                .build();
    }

    public MoodTag toMoodEntity(){
        return MoodTag.builder()
                .moodTagName(this.afterName)
                .build();
    }

    public GenreTag toGenreEntity(){
        return GenreTag.builder()
                .genreTagName(this.afterName)
                .build();
    }
}

package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class MusicMoodTagId implements Serializable {
    private int musicId;
    private int moodTagId;
}
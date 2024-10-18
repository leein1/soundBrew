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
public class MusicGenreTagId implements Serializable {
    private int music_id;
    private int music_genre_tag;
}


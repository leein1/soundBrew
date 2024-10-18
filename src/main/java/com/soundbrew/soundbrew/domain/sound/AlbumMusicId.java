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
public class AlbumMusicId implements Serializable {
    private int album_id;

    private int user_id;

    private int music_id;
}

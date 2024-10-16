package com.soundbrew.soundbrew.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class MusicInstrumentTagId implements Serializable {
    private int music_id;
    private int instrument_tag_id;

}

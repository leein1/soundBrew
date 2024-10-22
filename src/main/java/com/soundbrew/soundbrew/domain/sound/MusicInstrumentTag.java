package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MusicInstrumentTag {

    @EmbeddedId
    private MusicInstrumentTagId id;

    @ManyToOne
    @MapsId("musicId")
    @JoinColumn(name = "musicId")
    private Music music;

    @ManyToOne
    @MapsId("instrumentTagId")
    @JoinColumn(name = "instrumentTagId")
    private InstrumentTag instrumentTag;
}

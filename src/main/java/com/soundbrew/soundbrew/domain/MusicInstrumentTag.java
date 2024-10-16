package com.soundbrew.soundbrew.domain;

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
    @MapsId("music_id")
    @JoinColumn(name = "music_id")
    private Music music;

    @ManyToOne
    @MapsId("instrument_tag_id")
    @JoinColumn(name = "instrument_tag_id")
    private InstrumentTag instrumentTag;
}

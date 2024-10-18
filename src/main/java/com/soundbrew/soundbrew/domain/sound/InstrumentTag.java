package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(of = {"instrument_tag_id", "instrument_tag_name"})
public class InstrumentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instrument_tag_id;

    @Column(nullable = false)
    private String instrument_tag_name;

    @OneToMany(mappedBy = "instrumentTag")
    private List<MusicInstrumentTag> musicInstrumentTag;
}

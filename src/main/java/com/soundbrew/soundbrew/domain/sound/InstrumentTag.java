package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(of = {"instrumentTagId", "instrumentTagName"})
public class InstrumentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instrumentTagId;

    @Column(nullable = false)
    private String instrumentTagName;

    @OneToMany(mappedBy = "instrumentTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicInstrumentTag> musicInstrumentTag;

    public void update(String instrumentTagName){
        this.instrumentTagName = instrumentTagName;
    }

//    public void update(TagNameDto tagNameDto){
//        this.instrumentTagName = tagNameDto.getAfterName();
//    }
}

package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MusicMoodTag {
    @EmbeddedId
    private MusicMoodTagId id;

    @ManyToOne
    @MapsId("musicId")
    @JoinColumn(name = "musicId")
    private Music music;

    @ManyToOne
    @MapsId("moodTagId")
    @JoinColumn(name = "moodTagId")
    private MoodTag moodTag;
}

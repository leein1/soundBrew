package com.soundbrew.soundbrew.domain;

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
    @MapsId("music_id")
    @JoinColumn(name = "music_id")
    private Music music;

    @ManyToOne
    @MapsId("mood_tag_id")
    @JoinColumn(name = "mood_tag_id")
    private MoodTag moodTag;
}

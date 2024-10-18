package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(of = {"mood_tag_id", "mood_tag_name"})
public class MoodTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mood_tag_id;

    @Column(nullable = false)
    private String mood_tag_name;

    @OneToMany(mappedBy = "moodTag")
    private List<MusicMoodTag> musicMoodTag;
}

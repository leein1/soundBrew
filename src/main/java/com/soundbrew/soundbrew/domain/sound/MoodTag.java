package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(of = {"moodTagId", "moodTagName"})
public class MoodTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int moodTagId;

    @Column(nullable = false)
    private String moodTagName;

    @OneToMany(mappedBy = "moodTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicMoodTag> musicMoodTag;

    public void update(String moodTagName){
        this.moodTagName = moodTagName;
    }
}

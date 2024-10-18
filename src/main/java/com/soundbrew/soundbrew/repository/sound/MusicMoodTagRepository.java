package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicMoodTag;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicMoodTagRepository extends JpaRepository<MusicMoodTag, MusicMoodTagId> {
}

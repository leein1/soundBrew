package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodTagRepository extends JpaRepository<MoodTag, Integer> {
}

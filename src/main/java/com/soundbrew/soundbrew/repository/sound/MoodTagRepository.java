package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoodTagRepository extends JpaRepository<MoodTag, Integer> {
    Optional<MoodTag> findByMoodTagName(String tagName);
}

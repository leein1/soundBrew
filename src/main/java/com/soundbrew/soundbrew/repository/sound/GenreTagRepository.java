package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreTagRepository extends JpaRepository<GenreTag, Integer> {
}

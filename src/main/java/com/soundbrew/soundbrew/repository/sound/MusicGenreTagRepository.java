package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicGenreTag;
import com.soundbrew.soundbrew.domain.sound.MusicGenreTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicGenreTagRepository extends JpaRepository<MusicGenreTag, MusicGenreTagId> {
}

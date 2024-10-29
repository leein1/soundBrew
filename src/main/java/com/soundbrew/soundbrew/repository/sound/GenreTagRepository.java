package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreTagRepository extends JpaRepository<GenreTag, Integer> {
    Optional<GenreTag> findByGenreTagName(String tagName);
}

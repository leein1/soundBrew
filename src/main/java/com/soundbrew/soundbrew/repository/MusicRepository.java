package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Integer> {
}

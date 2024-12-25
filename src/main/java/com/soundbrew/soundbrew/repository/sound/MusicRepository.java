package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.repository.sound.custom.MusicRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Integer>, MusicRepositoryCustom {
    Optional<List<Music>> findByUserId(int userId);
}

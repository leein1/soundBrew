package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Integer> {
    Optional<List<Music>> findByUserId(int userId);
}

package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Optional<List<Album>> findByUserId(int userId);
}


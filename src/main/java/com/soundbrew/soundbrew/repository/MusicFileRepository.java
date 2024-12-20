package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicFileRepository extends JpaRepository<Music,Integer> {
    Optional<Music> findMusicByTitle(String title);
    Optional<Music> findMusicByFilePath(String filePath);
}

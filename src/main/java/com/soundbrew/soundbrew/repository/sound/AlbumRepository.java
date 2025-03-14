package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.repository.sound.custom.AlbumRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer>, AlbumRepositoryCustom {
    Optional<List<Album>> findByUserId(int userId);
    int countByUserId(int userId);
}


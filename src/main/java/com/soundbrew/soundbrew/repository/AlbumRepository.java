package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
}

package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlbumRepositoryCustom {
    Optional<Page<AlbumDto>> searchAll(String[] types, String keyword, Pageable pageable);
}

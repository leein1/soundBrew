package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.dto.sound.MusicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MusicRepositoryCustom {
    Optional<Page<MusicDto>> searchAll(String[] types, String keyword, Pageable pageable);
}

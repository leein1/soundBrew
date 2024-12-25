package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;

import java.util.Optional;

public interface MusicRepositoryCustom {
    Optional<SearchTotalResultDTO> soundOne(String nickname, String title);
    Optional<SearchTotalResultDTO> soundOne(int userId, int id);
}

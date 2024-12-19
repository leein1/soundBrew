package com.soundbrew.soundbrew.repository.sound.custom;

import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MusicRepositoryCustom {
    Optional<SearchTotalResultDto> soundOne(String nickname, String title);
    Optional<SearchTotalResultDto> soundOne(int userId, int id);
}

package com.soundbrew.soundbrew.repository.custom;

import com.soundbrew.soundbrew.dto.AlbumMusicAfterDto;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlbumMusicRepositoryCustom {
    List<AlbumMusicAfterDto> basicSearch(String nickname, Integer musicId, Integer albumId, Pageable pageable);
}

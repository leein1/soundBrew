package com.soundbrew.soundbrew.repository.custom;


import com.soundbrew.soundbrew.dto.sound.SoundSearchResultDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlbumMusicRepositoryCustom {
    List<SoundSearchResultDto> search(SoundSearchRequestDto soundSearchRequestDto, Pageable pageable);
}

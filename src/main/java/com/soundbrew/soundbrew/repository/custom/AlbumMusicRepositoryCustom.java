package com.soundbrew.soundbrew.repository.custom;


import com.soundbrew.soundbrew.dto.SoundRepositoryDto;
import com.soundbrew.soundbrew.dto.SoundRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlbumMusicRepositoryCustom {
    List<SoundRepositoryDto> search(SoundRequestDto soundRequestDto, Pageable pageable);
}

package com.soundbrew.soundbrew.repository.sound.custom;


import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.sound.SearchAlbumResultDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface AlbumMusicRepositoryCustom {
    Optional<Page<SearchTotalResultDto>> search(RequestDto requestDto);
    Optional<Page<SearchAlbumResultDto>> albums(RequestDto SearchRequestDto);
}

package com.soundbrew.soundbrew.repository.custom;


import com.soundbrew.soundbrew.dto.sound.SearchResultDto;
import com.soundbrew.soundbrew.dto.sound.SearchRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlbumMusicRepositoryCustom {
    List<SearchResultDto> search(SearchRequestDto searchRequestDto, Pageable pageable);
}

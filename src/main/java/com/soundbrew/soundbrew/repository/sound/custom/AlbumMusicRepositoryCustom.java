package com.soundbrew.soundbrew.repository.sound.custom;


import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface AlbumMusicRepositoryCustom {
    Optional<Page<SearchTotalResultDto>> search(RequestDto requestDto,String searchType);

    //get album..
    Optional<Page<SearchTotalResultDto>> getAlbumOne(String nickname, String albumName,RequestDto requestDto);
    Optional<Page<SearchTotalResultDto>> getAlbumOne(String nickname, int id, RequestDto requestDto);

}

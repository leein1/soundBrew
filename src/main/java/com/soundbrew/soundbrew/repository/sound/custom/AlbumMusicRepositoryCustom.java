package com.soundbrew.soundbrew.repository.sound.custom;


import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface AlbumMusicRepositoryCustom {
    Optional<Page<SearchTotalResultDto>> search(RequestDto requestDto);
    Optional<Page<SearchTotalResultDto>> searchAlbum(RequestDto requestDto);

    //get album..
    Optional<Page<SearchTotalResultDto>> albumOne(String nickname, String albumName,RequestDto requestDto);
    Optional<Page<SearchTotalResultDto>> albumOne(int userId, int id, RequestDto requestDto);

}

package com.soundbrew.soundbrew.repository.sound.custom;


import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface AlbumMusicRepositoryCustom {
    Optional<Page<SearchTotalResultDTO>> search(RequestDTO requestDTO);
    Optional<Page<SearchTotalResultDTO>> searchAlbum(RequestDTO requestDTO);

    //get album..
    Optional<Page<SearchTotalResultDTO>> albumOne(String nickname, String albumName, RequestDTO requestDTO);
    Optional<Page<SearchTotalResultDTO>> albumOne(int userId, int id, RequestDTO requestDTO);

}

package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;

public class DTOFilteringFactory {
    public static AlbumDTO hideAlbumDTO(AlbumDTO albumDTO){
        albumDTO.setAlbumId(0);
        albumDTO.setUserId(0);
        return albumDTO;
    }

    public static MusicDTO hideMusicDTO(MusicDTO musicDTO){
        musicDTO.setMusicId(0);
        musicDTO.setUserId(0);
        return musicDTO;
    }

    public static SearchTotalResultDTO hideSearchTotalResultDTO(SearchTotalResultDTO searchTotalResultDTO){
        if (searchTotalResultDTO.getAlbumDTO() != null) { // null 체크 추가
            searchTotalResultDTO.getAlbumDTO().setAlbumId(0);
        }

        if(searchTotalResultDTO.getMusicDTO() != null) {
            searchTotalResultDTO.getMusicDTO().setMusicId(0);
        }
        
        return searchTotalResultDTO;
    }

}

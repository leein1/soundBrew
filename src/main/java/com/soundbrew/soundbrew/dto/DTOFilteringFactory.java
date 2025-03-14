package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DTOFilteringFactory {
    public static AlbumDTO hideAlbumDTO(AlbumDTO albumDTO) {
        if (albumDTO == null) {
            return null;
        }
        albumDTO.setUserId(0);
        return albumDTO;
    }

    public static List<SearchTotalResultDTO> hideAlbumDTO(List<SearchTotalResultDTO> dtoList) {
        return dtoList.stream()
                .peek(dto -> dto.setAlbumDTO(hideAlbumDTO(dto.getAlbumDTO())))
                .collect(Collectors.toList());
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

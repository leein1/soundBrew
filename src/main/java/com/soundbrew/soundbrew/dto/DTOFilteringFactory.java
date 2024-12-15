package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;

public class DTOFilteringFactory {
    public static AlbumDto hideAlbumDto(AlbumDto albumDto){
        albumDto.setAlbumId(0);
        albumDto.setUserId(0);
        return albumDto;
    }

    public static MusicDto hideMusicDto(MusicDto musicDto){
        musicDto.setMusicId(0);
        musicDto.setUserId(0);
        return musicDto;
    }

    public static SearchTotalResultDto hideSearchTotalResultDto(SearchTotalResultDto searchTotalResultDto){
        searchTotalResultDto.setAlbumId(0);
        searchTotalResultDto.setMusicId(0);
        return searchTotalResultDto;
    }

}

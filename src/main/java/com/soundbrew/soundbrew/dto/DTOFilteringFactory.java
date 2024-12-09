package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;

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

}

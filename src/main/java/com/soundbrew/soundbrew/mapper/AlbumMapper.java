package com.soundbrew.soundbrew.mapper;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import org.springframework.stereotype.Component;

import static com.soundbrew.soundbrew.service.util.FilteringUtil.filteringTrim;

@Component
public class AlbumMapper {

    public Album toEntity(AlbumDto albumDto) {
        return Album.builder()
                .albumId(albumDto.getAlbumId())
                .userId(albumDto.getUserId())
                .albumArtPath(albumDto.getAlbumArtPath())
                .albumName(filteringTrim(albumDto.getAlbumName()))
                .description(filteringTrim(albumDto.getDescription()))
                .build();
    }

    public AlbumDto toDto(Album album) {
        return AlbumDto.builder()
                .albumId(album.getAlbumId())
                .userId(album.getUserId())
                .albumName(filteringTrim(album.getAlbumName()))
                .albumArtPath(album.getAlbumArtPath())
                .description(album.getDescription())
                .build();
    }
}


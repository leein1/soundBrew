package com.soundbrew.soundbrew.mapper;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import org.springframework.stereotype.Component;

import static com.soundbrew.soundbrew.service.util.FilteringUtil.filteringWord;

@Component
public class AlbumMapper {

    public Album toEntity(AlbumDto albumDto) {
        return Album.builder()
                .albumId(albumDto.getAlbumId())
                .userId(albumDto.getUserId())
                .albumArtPath(albumDto.getAlbumArtPath())
                .albumName(filteringWord(albumDto.getAlbumName(), "앨범"))
                .description(filteringWord(albumDto.getDescription(), "기본"))
                .build();
    }

    public AlbumDto toDto(Album album) {
        return AlbumDto.builder()
                .albumId(album.getAlbumId())
                .userId(album.getUserId())
                .albumName(album.getAlbumName())
                .albumArtPath(album.getAlbumArtPath())
                .description(album.getDescription())
                .build();
    }
}


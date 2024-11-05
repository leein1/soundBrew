package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AlbumDto {
    private int albumId;
    private int userId;
    private String albumName;
    private String albumArtPath;
    private String description;

    public Album toEntity() {
        return Album.builder()
                .userId(this.userId)
                .albumName(this.albumName)
                .albumArtPath(this.albumArtPath)
                .description(this.description)
                .build();
    }
}

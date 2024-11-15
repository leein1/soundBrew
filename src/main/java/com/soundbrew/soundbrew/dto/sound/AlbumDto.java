package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.domain.sound.AlbumMusicId;
import com.soundbrew.soundbrew.domain.sound.Music;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumDto extends BaseEntityDto{
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

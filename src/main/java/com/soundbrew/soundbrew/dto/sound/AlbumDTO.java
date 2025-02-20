package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumDTO extends BaseEntityDTO {
    private int albumId;
    private int userId;
    private String nickname;

    @NotBlank
    @Size(min = 2, max = 50)
    private String albumName;

    @NotBlank
    @Size(min = 2, max = 255)
    private String albumArtPath;

    @NotBlank
    @Size(max = 500)
    private String description;

    @Column
    private int download;

    public AlbumDTO(int albumId, int userId, String albumName, String albumArtPath, String description, String nickname, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.albumId = albumId;
        this.userId = userId;
        this.albumName = albumName;
        this.albumArtPath = albumArtPath;
        this.description = description;
        this.nickname = nickname;
        super.setCreateDate(createDate); // BaseEntityDto의 필드 설정
        super.setModifyDate(modifyDate);
    }

    public Album toEntity(List<AlbumMusic> albumMusic) {
        return Album.builder()
                .albumId(this.albumId)
                .userId(this.userId)
                .nickname(this.nickname)
                .albumName(this.albumName)
                .albumArtPath(this.albumArtPath)
                .description(this.description)
                .albumMusic(albumMusic)
                .build();
    }
}

package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumDTO extends BaseEntityDTO {
    private int albumId;
    private int userId;
    private String nickname;
    private String albumName;
    private String albumArtPath;
    private String description;

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

    public Album toEntity() {
        return Album.builder()
                .userId(this.userId)
                .nickname(this.nickname)
                .albumName(this.albumName)
                .albumArtPath(this.albumArtPath)
                .description(this.description)
                .build();
    }
}

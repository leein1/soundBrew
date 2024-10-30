package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import lombok.Builder;
import lombok.Data;

import static com.soundbrew.soundbrew.service.util.FilteringUtil.filteringTrim;


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

//    public Album toEntityUpdate(){
//        return Album.builder()
//                .albumName(this.albumName)
//                .description(this.description)
//                .build();
//    }

//    첫번째 filtering 방법 - Dto에서 필터링 메서드 생성 후 호출.
    public void filterFields() {
        this.albumName = filteringTrim(this.albumName);
        this.description = filteringTrim(this.description);
    }

}

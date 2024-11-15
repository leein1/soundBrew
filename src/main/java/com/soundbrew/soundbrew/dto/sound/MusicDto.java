package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicDto extends BaseEntityDto{
    private int musicId;
    private int userId;
    private String title;
    private String filePath;
    private int price;
    private String description;
    private String soundType;

    public Music toEntity(){
        return Music.builder()
                .musicId(this.musicId)
                .userId(this.userId)
                .title(this.title)
                .filePath(this.filePath)
                .price(this.price)
                .description(this.description)
                .soundType(this.soundType)
                .build();
    }
}

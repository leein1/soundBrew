package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.BaseEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicDto extends BaseEntityDto {
    private int musicId;
    private int userId;
    private String nickname;
    private String title;
    private String filePath;
    private int price;
    private String description;
    private String soundType;


    public MusicDto(int musicId, int userId, String title, String filePath, int price, String description, String soundType, String nickname, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.musicId = musicId;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.filePath = filePath;
        this.price = price;
        this.description = description;
        this.soundType = soundType;
        super.setCreate_date(createDate); // BaseEntityDto의 필드 설정
        super.setModify_date(modifyDate);
    }


    public Music toEntity(){
        return Music.builder()
                .musicId(this.musicId)
                .userId(this.userId)
                .nickname(this.nickname)
                .title(this.title)
                .filePath(this.filePath)
                .price(this.price)
                .description(this.description)
                .soundType(this.soundType)
                .build();
    }
}

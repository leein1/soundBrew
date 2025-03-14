package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.BaseDTO;
import com.soundbrew.soundbrew.dto.BaseEntityDTO;
import com.soundbrew.soundbrew.util.valid.CreateGroup;
import com.soundbrew.soundbrew.util.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicDTO extends BaseDTO {
    private int musicId;
    private int userId;
    private String nickname;
    private int price;
    private String soundType;

    @NotBlank(groups = {CreateGroup.class})
    @Size(min = 2, max = 50,groups = {CreateGroup.class})
    private String title;

    @NotBlank(groups = {CreateGroup.class})
    @Size(min = 2, max = 255,groups = {CreateGroup.class})
    private String filePath;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 500,groups = {CreateGroup.class, UpdateGroup.class})
    private String description;

    @Column
    private int download;

    public MusicDTO(int musicId, int userId, String title, String filePath, int price, String description, String soundType, String nickname, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.musicId = musicId;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.filePath = filePath;
        this.price = price;
        this.description = description;
        this.soundType = soundType;
        super.setCreateDate(createDate); // BaseEntityDto의 필드 설정
        super.setModifyDate(modifyDate);
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

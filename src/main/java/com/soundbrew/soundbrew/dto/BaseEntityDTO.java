package com.soundbrew.soundbrew.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntityDTO {
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}

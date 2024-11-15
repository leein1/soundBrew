package com.soundbrew.soundbrew.dto.sound;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class BaseEntityDto {
    private LocalDateTime create_date;
    private LocalDateTime modify_date;
}

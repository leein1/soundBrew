package com.soundbrew.soundbrew.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntityDto {
    private LocalDateTime create_date;
    private LocalDateTime modify_date;


}

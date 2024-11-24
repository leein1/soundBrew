package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data

public abstract class BaseDTO {

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

}

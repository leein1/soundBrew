package com.soundbrew.soundbrew.dto.statistics.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor  // (String, long) 생성자 추가
public class TagStatisticDTO {
    private String tagName;
    private long count;
}

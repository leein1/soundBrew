package com.soundbrew.soundbrew.dto.statistics.sound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SoundStatisticDTO {
    private long userId;
    private long musicId;
    private String title;
    private String nickname;
    private long count;
    private long download;
}

package com.soundbrew.soundbrew.dto.sound;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreamingDto {
    private byte[] data;
    private long start;
    private long end;
    private long fileLength;
}

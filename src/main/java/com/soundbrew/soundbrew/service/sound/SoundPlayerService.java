package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.StreamingDto;
import org.springframework.http.HttpRange;

import java.io.IOException;

public interface SoundPlayerService {
    StreamingDto streamSound(HttpRange range, String fileName) throws IOException;
}

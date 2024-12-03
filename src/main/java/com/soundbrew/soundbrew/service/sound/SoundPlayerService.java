package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.SoundStreamDto;
import org.springframework.http.HttpRange;

import java.io.IOException;
import java.util.Optional;

public interface SoundPlayerService {
    Optional<SoundStreamDto> streamSound(HttpRange range, String fileName) throws IOException;
}

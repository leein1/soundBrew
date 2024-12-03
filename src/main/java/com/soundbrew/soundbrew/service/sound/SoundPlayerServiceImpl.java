package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.SoundStreamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoundPlayerServiceImpl implements SoundPlayerService {

    private final ResourceLoader resourceLoader;

    @Value("${player.sounds}")
    private String fileDirectory;

    private static final long MAX_RANGE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public Optional<SoundStreamDto> streamSound(HttpRange range, String fileName) throws IOException {
        // 파일 경로 확인 및 존재 여부 검사
        Path filePath = Path.of(fileDirectory+"/"+fileName);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) return Optional.empty();

        long fileLength = Files.size(filePath);
        long start = range.getRangeStart(fileLength);
        long end = Math.min(start + MAX_RANGE_SIZE - 1, fileLength - 1);
        if (start >= fileLength) return Optional.empty();

        long rangeLength = end - start + 1;
        byte[] data = new byte[(int) rangeLength];

        // 파일에서 해당 범위 데이터를 읽어오기
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            raf.seek(start);
            raf.read(data, 0, (int) rangeLength);
        }

        // 데이터 반환
        return Optional.of(new SoundStreamDto(data, start, end, fileLength));
    }
}

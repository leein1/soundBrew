package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.StreamingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class SoundPlayerServiceImpl implements SoundPlayerService {

    private final ResourceLoader resourceLoader;

    @Value("${player.sounds}")
    private String fileDirectory;

    private static final long MAX_RANGE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public StreamingDto streamSound(HttpRange range, String fileName) throws IOException {
        // 파일 경로 확인 및 존재 여부 검사
        Path filePath = Path.of(fileDirectory+"/"+fileName);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new IllegalArgumentException("File not found or is not readable: " + fileName);
        }

        long fileLength = Files.size(filePath);

        // Range 헤더에서 시작점만 추출
        long start = range.getRangeStart(fileLength);
        long end = Math.min(start + MAX_RANGE_SIZE - 1, fileLength - 1);

        if (start >= fileLength) {
            throw new IllegalArgumentException("Invalid range: Start position exceeds file length");
        }

        long rangeLength = end - start + 1;
        byte[] data = new byte[(int) rangeLength];

        // 파일에서 해당 범위 데이터를 읽어오기
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            raf.seek(start);
            raf.read(data, 0, (int) rangeLength);
        }

        // 데이터 반환
        return new StreamingDto(data, start, end, fileLength);
    }
}

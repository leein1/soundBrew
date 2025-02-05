package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.sound.SoundStreamDTO;
import com.soundbrew.soundbrew.service.file.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stream")
@Log4j2
public class FileStreamController {
    private final FileService fileService;

    @GetMapping("/{fileName}")
    ResponseEntity<byte[]> streamSound(@RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader, @PathVariable String fileName) throws IOException {
        final long FIXED_RANGE_SIZE = 2 * 1024 * 1024; // 2 MB
        HttpRange range;

        System.err.println("fileName : "+fileName);
        System.err.println("fileName : "+fileName);
        System.err.println("fileName : "+fileName);
        // Range 헤더와 관계없이 항상 고정 크기 2MB 범위를 설정
        long start = 0;
        if (rangeHeader != null) {
            // 클라이언트가 Range 헤더를 보냈을 경우 시작 위치를 계산
            start = HttpRange.parseRanges(rangeHeader).stream().findFirst().orElseThrow().getRangeStart(Long.MAX_VALUE);
        }

        range = HttpRange.createByteRange(start, start + FIXED_RANGE_SIZE - 1);

        // 서비스 호출
        SoundStreamDTO dto = fileService.streamSound(range, fileName).getDto();

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dto.getEnd() - dto.getStart() + 1))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + dto.getStart() + "-" + dto.getEnd() + "/" + dto.getFileLength())
                .body(dto.getData());
    }
}

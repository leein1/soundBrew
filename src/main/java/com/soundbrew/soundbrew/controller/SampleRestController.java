package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.sound.StreamingDto;
import com.soundbrew.soundbrew.service.sound.SoundPlayerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class SampleRestController {
    private final SoundPlayerServiceImpl soundPlayerService;

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<byte[]> streamSound(@RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader, @PathVariable String fileName) {
        // Range 헤더가 없는 경우 처리
        if (rangeHeader == null || rangeHeader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.WARNING, "Range header is required for streaming.")
                    .body("Invalid request: Range header missing.".getBytes());
        }

        // 다중 Range 비허용 검사
        HttpRange range = HttpRange.parseRanges(rangeHeader).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid Range header"));

        if (HttpRange.parseRanges(rangeHeader).size() > 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.WARNING, "Multiple ranges are not supported.")
                    .body("Invalid request: Multiple ranges are not allowed.".getBytes());
        }

        try {
            StreamingDto responseDto = soundPlayerService.streamSound(range, fileName);

            // 응답 헤더 설정 및 데이터 반환
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(responseDto.getEnd() - responseDto.getStart() + 1))
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + responseDto.getStart() + "-" + responseDto.getEnd() + "/" + responseDto.getFileLength())
                    .body(responseDto.getData());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.WARNING, "Error reading file")
                    .body(("Failed to stream file: " + ex.getMessage()).getBytes());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header(HttpHeaders.WARNING, ex.getMessage())
                    .build();
        }
    }
}

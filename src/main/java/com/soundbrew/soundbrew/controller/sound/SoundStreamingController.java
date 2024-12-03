package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.SoundStreamDto;
import com.soundbrew.soundbrew.service.sound.SoundPlayerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SoundStreamingController {
    private final SoundPlayerServiceImpl soundPlayerService;

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<byte[]> streamSound(@RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader, @PathVariable String fileName) throws IOException {
        // Range 헤더가 없는 경우 처리
        if (rangeHeader == null || rangeHeader.isEmpty()) return ResponseEntity.badRequest().build();

        // 다중 Range 비허용 검사
        HttpRange range = HttpRange.parseRanges(rangeHeader).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid Range header"));
        if (HttpRange.parseRanges(rangeHeader).size() > 1) return ResponseEntity.badRequest().build();

        Optional<SoundStreamDto> responseDto = soundPlayerService.streamSound(range, fileName);
        if(responseDto.isEmpty()) return ResponseEntity.badRequest().build();

        // 응답 헤더 설정 및 데이터 반환
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(responseDto.get().getEnd() - responseDto.get().getStart() + 1))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + responseDto.get().getStart() + "-" + responseDto.get().getEnd() + "/" + responseDto.get().getFileLength())
                .body(responseDto.get().getData());
    }
}

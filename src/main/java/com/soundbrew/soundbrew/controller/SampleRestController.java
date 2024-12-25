package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDto;
import com.soundbrew.soundbrew.service.SoundsService;
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
    private final SoundsService soundsService;

    @GetMapping("/stream/{fileName}")
    ResponseEntity<byte[]> streamSound(@RequestHeader(HttpHeaders.RANGE) String rangeHeader, @PathVariable String fileName) throws IOException {
        HttpRange range = HttpRange.parseRanges(rangeHeader).stream()
                .findFirst()
                .orElseThrow();

        ResponseDto<SoundStreamDto> responseDto = soundsService.streamSound(range, fileName);
        SoundStreamDto dto = responseDto.getDto();

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dto.getEnd() - dto.getStart() + 1))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + dto.getStart() + "-" + dto.getEnd() + "/" + dto.getFileLength())
                .body(dto.getData());
    }
}

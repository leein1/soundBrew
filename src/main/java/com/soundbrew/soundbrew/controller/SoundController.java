package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.service.SoundsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sounds")
public class SoundController {
    private final SoundsService soundsService;

    //    @GetMapping("/stream/{fileName}")
//    public ResponseEntity<byte[]> streamSound(@RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader, @PathVariable String fileName) throws IOException {
//        // Range 헤더가 없는 경우 처리
//        if (rangeHeader == null || rangeHeader.isEmpty()) return ResponseEntity.badRequest().build();
//
//        // 다중 Range 비허용 검사
//        HttpRange range = HttpRange.parseRanges(rangeHeader).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid Range header"));
//        if (HttpRange.parseRanges(rangeHeader).size() > 1) return ResponseEntity.badRequest().build();
//
//        ResponseDto<SoundStreamDto> responseDto = soundsService.streamSound(range, fileName);
//
//        // 응답 헤더 설정 및 데이터 반환
//        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
//                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(responseDto.getDto().getEnd() - responseDto.getDto().getStart() + 1))
//                .header(HttpHeaders.CONTENT_RANGE, "bytes " + responseDto.getDto().getStart() + "-" + responseDto.getDto().getEnd() + "/" + responseDto.getDto().getFileLength())
//                .body(responseDto.getDto().getData());
//    }

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

    @GetMapping("/tracks")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> totalSoundSearch(@ModelAttribute RequestDto requestDto) {
        ResponseDto<SearchTotalResultDto> responseDto = soundsService.totalSoundSearch(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> totalAlbumSearch(RequestDto requestDto) {
        ResponseDto<SearchTotalResultDto> responseDto = soundsService.totalAlbumSearch(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tags")
    ResponseEntity<ResponseDto<TagsDto>> totalTagsSearch(@RequestBody Map<String, List<SearchTotalResultDto>> requestBody) {
        List<SearchTotalResultDto> sounds = null;
        if (requestBody.containsKey("dtoList")) sounds = requestBody.get("dtoList");
        else if (requestBody.containsKey("dto")) sounds = requestBody.get("dto");

        ResponseDto<TagsDto> responseDto = soundsService.totalTagsSearch(sounds);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{nickname}/title/{title}")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getSoundsOne(@PathVariable String nickname,@PathVariable String title){
        ResponseDto<SearchTotalResultDto> responseDto = soundsService.getSoundOne(nickname,title);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{nickname}/title/{albumName}")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getAlbumsOne(@PathVariable String nickname, @PathVariable String albumName, RequestDto requestDto){
        ResponseDto<SearchTotalResultDto> responseDto = soundsService.getAlbumOne(nickname,albumName,requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

}

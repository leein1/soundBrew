package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
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
//        ResponseDTO<SoundStreamDTO> responseDto = soundsService.streamSound(range, fileName);
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

        ResponseDTO<SoundStreamDTO> responseDto = soundsService.streamSound(range, fileName);
        SoundStreamDTO dto = responseDto.getDto();

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dto.getEnd() - dto.getStart() + 1))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + dto.getStart() + "-" + dto.getEnd() + "/" + dto.getFileLength())
                .body(dto.getData());
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> totalSoundSearch(@ModelAttribute RequestDTO requestDto) {
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.totalSoundSearch(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> totalAlbumSearch(RequestDTO requestDto) {
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.totalAlbumSearch(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tags")
    ResponseEntity<ResponseDTO<TagsDTO>> totalTagsSearch(@RequestBody Map<String, List<SearchTotalResultDTO>> requestBody) {
        List<SearchTotalResultDTO> sounds = null;
        if (requestBody.containsKey("dtoList")) sounds = requestBody.get("dtoList");
        else if (requestBody.containsKey("dto")) sounds = requestBody.get("dto");

        ResponseDTO<TagsDTO> responseDto = soundsService.totalTagsSearch(sounds);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{nickname}/title/{title}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundsOne(@PathVariable String nickname, @PathVariable String title){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOne(nickname,title);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{nickname}/title/{albumName}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumsOne(@PathVariable String nickname, @PathVariable String albumName, RequestDTO requestDto){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOne(nickname,albumName,requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

}

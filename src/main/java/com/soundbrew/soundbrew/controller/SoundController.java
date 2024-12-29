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

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> totalSoundSearch(@ModelAttribute RequestDTO requestDto) {
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.totalSoundSearch(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> totalAlbumSearch(@ModelAttribute RequestDTO requestDto) {
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.totalAlbumSearch(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tags")
    ResponseEntity<ResponseDTO<TagsDTO>> totalTagsSearch(@RequestBody Map<String, List<SearchTotalResultDTO>> requestBody) { // 검색한 결과를 바탕으로 태그를 하나하나 담은 리스트 결과화.
        List<SearchTotalResultDTO> sounds = requestBody.containsKey("dtoList") ? requestBody.get("dtoList") : requestBody.get("dto");

        ResponseDTO<TagsDTO> responseDto = tagsService.totalTagsSearch(sounds);

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

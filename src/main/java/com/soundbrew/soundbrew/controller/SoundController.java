package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.service.SoundsService;
import com.soundbrew.soundbrew.service.TagsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sounds")
public class SoundController {
    private final SoundsService soundsService;
    private final TagsService tagsService;

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

    @GetMapping("/tags") // 메인 페이지의 전체 태그 버튼들
    ResponseEntity<ResponseDTO<TagsDTO>> totalTagsSearch(@ModelAttribute RequestDTO requestDTO){
        ResponseDTO<TagsDTO> responseDTO = tagsService.getAllTags(requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/tags") // 특정 앨범, 특정 곡의 전체 태그 버튼들
    ResponseEntity<ResponseDTO<TagsDTO>> tagsSearchAfter(@RequestBody Map<String, List<SearchTotalResultDTO>> requestBody) {

        // 검색한 결과를 바탕으로 태그를 하나하나 담은 리스트 결과화.
        List<SearchTotalResultDTO> sounds = requestBody.containsKey("dtoList") ? requestBody.get("dtoList") : requestBody.get("dto");

        ResponseDTO<TagsDTO> responseDto = tagsService.tagsSearchAfter(sounds);

        return ResponseEntity.ok().body(responseDto);
    }

    // ** 음원들과 매핑 되어있는 전체 태그를 표시 **
    @GetMapping("/tags/mapped") // 메인 페이지의 전체 태그 버튼들
    ResponseEntity<ResponseDTO<TagsDTO>> totalTagsSearch(@ModelAttribute RequestDTO requestDTO){
        ResponseDTO<TagsDTO> responseDTO = tagsService.getAllTags(requestDTO);
    }

    @GetMapping("/tracks/{nickname}/title/{title}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundsOne(@PathVariable String nickname, @PathVariable String title){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOne(nickname,title);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{nickname}/title/{albumName}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumsOne(@PathVariable String nickname, @PathVariable String albumName, @ModelAttribute RequestDTO requestDto){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOne(nickname,albumName,requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // ** 그냥 존재하는 모든 태그들을 표시 **
    @GetMapping("/tags") // 관리자모드 태그 정보
    ResponseEntity<ResponseDTO<TagsDTO>> getTags(){
        ResponseDTO<TagsDTO> responseDto = tagsService.getTags();

        return  ResponseEntity.ok().body(responseDto);
    }

}

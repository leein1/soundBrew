package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.service.sound.SoundsService;
import com.soundbrew.soundbrew.service.tag.TagsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sounds")
@Validated
public class SoundController {
    private final SoundsService soundsService;
    private final TagsService tagsService;

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> totalSoundSearch(@ModelAttribute  @Valid RequestDTO requestDto) {
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.totalSoundSearch(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> totalAlbumSearch(@ModelAttribute  @Valid RequestDTO requestDto) {
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.totalAlbumSearch(requestDto);

        return  ResponseEntity.ok().body(responseDto);
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
    ResponseEntity<ResponseDTO<TagsDTO>> totalTagsSearch(@ModelAttribute  @Valid RequestDTO requestDTO){
        ResponseDTO<TagsDTO> responseDTO = tagsService.getAllTags(requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/tracks/{nickname}/title/{title}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundsOne(@PathVariable @Size(min = 2, max = 37) String nickname, @PathVariable @Size(min = 2, max = 50) String title){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOne(nickname,title);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{nickname}/title/{albumName}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumsOne(@PathVariable @Size(min = 2, max = 37) String nickname,  @PathVariable @Size(min = 2, max = 255) String albumName, @ModelAttribute @Valid RequestDTO requestDto){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOne(nickname,albumName,requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // ** 그냥 존재하는 모든 태그들을 표시 **
    @GetMapping("/tags")
    ResponseEntity<ResponseDTO<TagsDTO>> getTags(){
        ResponseDTO<TagsDTO> responseDto = tagsService.getTags();

        return  ResponseEntity.ok().body(responseDto);
    }

}

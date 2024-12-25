package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.MeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.readers.operation.ResponseMessagesReader;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/me")
@Log4j2
public class MeController {
    private final MeService meService;

    @PostMapping("/tracks/{musicId}/tags")
    ResponseEntity<ResponseDto> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDto tagsDto){
        ResponseDto responseDto = meService.updateLinkTags(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // ResponseDto<TagsDto> tagsList(RequestDto requestDto){

    // sounds for me
    @PostMapping("/sounds")
    ResponseEntity<ResponseDto> createSound(@RequestBody SoundCreateDto soundCreateDto){
        //내 회원 id 들고오기
        AlbumDto albumDto = soundCreateDto.getAlbumDto();
        MusicDto musicDto = soundCreateDto.getMusicDto();
        TagsDto tagsDto = soundCreateDto.getTagsDto();

        ResponseDto responseDto = meService.createSound(2,albumDto,musicDto,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDto> updateAlbum(@PathVariable int albumId,@RequestBody AlbumDto albumDto){
        ResponseDto responseDto = meService.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDto> updateMusic(@PathVariable int musicId,@RequestBody MusicDto musicDto ){
        ResponseDto responseDto = meService.updateMusic(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getSoundOne(@PathVariable("musicId") int id){
        // 쿠키 -> 내이름
        ResponseDto<SearchTotalResultDto> responseDto = meService.getSoundOne(2,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{albumId}")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getAlbumOne(@PathVariable("albumId") int id, RequestDto requestDto){
        //쿠키 -> 내이름
        ResponseDto<SearchTotalResultDto> responseDto = meService.getAlbumOne(2,id,requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getSoundMe(RequestDto requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDto<SearchTotalResultDto> responseDto = meService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tags")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getTagsMe(RequestDto requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDto<SearchTotalResultDto> responseDto = meService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getAlbumMe(RequestDto requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDto<SearchTotalResultDto> responseDto = meService.getAlbumMe(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }
}

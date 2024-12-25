package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.MeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/me")
@Log4j2
public class MeController {
    private final MeService meService;

    @PostMapping("/tracks/{musicId}/tags")
    ResponseEntity<ResponseDTO> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDTO tagsDto){
        ResponseDTO responseDto = meService.updateLinkTags(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // ResponseDTO<TagsDTO> tagsList(RequestDTO requestDto){

    // sounds for me
    @PostMapping("/sounds")
    ResponseEntity<ResponseDTO> createSound(@RequestBody SoundCreateDTO soundCreateDto){
        //내 회원 id 들고오기
        AlbumDTO albumDto = soundCreateDto.getAlbumDTO();
        MusicDTO musicDto = soundCreateDto.getMusicDTO();
        TagsDTO tagsDto = soundCreateDto.getTagsDTO();

        ResponseDTO responseDto = meService.createSound(2,albumDto,musicDto,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDTO albumDto){
        ResponseDTO responseDto = meService.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> updateMusic(@PathVariable int musicId, @RequestBody MusicDTO musicDto ){
        ResponseDTO responseDto = meService.updateMusic(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable("musicId") int id){
        // 쿠키 -> 내이름
        ResponseDTO<SearchTotalResultDTO> responseDto = meService.getSoundOne(2,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable("albumId") int id, RequestDTO requestDto){
        //쿠키 -> 내이름
        ResponseDTO<SearchTotalResultDTO> responseDto = meService.getAlbumOne(2,id,requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundMe(RequestDTO requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = meService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tags")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getTagsMe(RequestDTO requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = meService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumMe(RequestDTO requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = meService.getAlbumMe(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }
}

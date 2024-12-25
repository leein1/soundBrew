package com.soundbrew.soundbrew.controller;


import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.service.AdminService;
import com.soundbrew.soundbrew.service.AdminServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminServiceImpl adminServiceImpl;

    @PostMapping("/tags")
    ResponseEntity<ResponseDTO> createTag(@RequestBody TagsDTO tagsDto){
        ResponseDTO responseDto = adminService.createTag(tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/instruments/{beforeName}")
    ResponseEntity<ResponseDTO> updateInstrumentTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
        ResponseDTO responseDto  = adminService.updateInstrumentTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/moods/{beforeName}")
    ResponseEntity<ResponseDTO> updateMoodTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
        ResponseDTO responseDto = adminService.updateMoodTagSpelling(beforeName, afterName);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/genres/{beforeName}")
    ResponseEntity<ResponseDTO> updateGenreTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
            ResponseDTO responseDto = adminService.updateGenreTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tags/{musicId}")
    ResponseEntity<ResponseDTO> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDTO tagsDto){
        ResponseDTO responseDto = adminServiceImpl.updateLinkTags(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tags")
    ResponseEntity<ResponseDTO<TagsDTO>> getTags(@RequestParam(required = false) List<Integer> musicIds){ // 아티스트(내)의 음악들 ( List<Integer>)의 태그들
        ResponseDTO<TagsDTO> responseDto = adminServiceImpl.getTags(musicIds);

        return  ResponseEntity.ok().body(responseDto);
    }

    // sounds for admin
    @DeleteMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> deleteAlbum(@PathVariable int albumId){
        ResponseDTO responseDto = adminServiceImpl.deleteAlbum(albumId);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> deleteMusic(@PathVariable int musicId){
        ResponseDTO responseDto = adminServiceImpl.deleteMusic(musicId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDTO albumDto){
        ResponseDTO responseDto = adminServiceImpl.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> updateMusic(@PathVariable int musicId, @RequestBody MusicDTO musicDto ){
        ResponseDTO responseDto = adminServiceImpl.updateMusic(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{userId}/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable int userId, @PathVariable("musicId") int id){
        ResponseDTO<SearchTotalResultDTO> responseDto = adminServiceImpl.getSoundOne(userId,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{userId}/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable int userId, @PathVariable("albumId") int id){
        ResponseDTO<SearchTotalResultDTO> responseDto = adminServiceImpl.getAlbumOne(userId,id);

        return ResponseEntity.ok().body(responseDto);
    }
}

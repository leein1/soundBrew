package com.soundbrew.soundbrew.controller;


import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/tags")
    ResponseEntity<ResponseDto> createTag(@RequestBody TagsDto tagsDto){
        ResponseDto responseDto = adminService.createTag(tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/instruments/{beforeName}")
    ResponseEntity<ResponseDto> updateInstrumentTagSpelling(@PathVariable String beforeName,@RequestBody String afterName){
        ResponseDto responseDto  = adminService.updateInstrumentTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/moods/{beforeName}")
    ResponseEntity<ResponseDto> updateMoodTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
        ResponseDto responseDto = adminService.updateMoodTagSpelling(beforeName, afterName);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/genres/{beforeName}")
    ResponseEntity<ResponseDto> updateGenreTagSpelling(@PathVariable String beforeName,@RequestBody String afterName){
            ResponseDto responseDto = adminService.updateGenreTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tags/{musicId}")
    ResponseEntity<ResponseDto> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDto tagsDto){
        ResponseDto responseDto = adminService.updateLinkTags(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tags")
    ResponseEntity<ResponseDto<TagsDto>> getTags(@RequestBody List<Integer> musicIds){ // 아티스트(내)의 음악들 ( List<Integer>)의 태그들
        ResponseDto<TagsDto> responseDto = adminService.getTags(musicIds);

        return  ResponseEntity.ok().body(responseDto);
    }

    // sounds for admin
    @DeleteMapping("/albums/{albumId}")
    ResponseEntity<ResponseDto> deleteAlbum(@PathVariable int albumId){
        ResponseDto responseDto = adminService.deleteAlbum(albumId);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDto> deleteMusic(@PathVariable int musicId){
        ResponseDto responseDto = adminService.deleteMusic(musicId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDto> updateAlbum(@PathVariable int albumId,@RequestBody AlbumDto albumDto){
        ResponseDto responseDto = adminService.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDto> updateMusic(@PathVariable int musicId,@RequestBody MusicDto musicDto ){
        ResponseDto responseDto = adminService.updateMusic(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{nickname}/{musicId}")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getSoundOne(@PathVariable String nickname, @PathVariable("musicId") int id){
        ResponseDto<SearchTotalResultDto> responseDto = adminService.getSoundOne(nickname,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{nickname}/{albumId}")
    ResponseEntity<ResponseDto<SearchTotalResultDto>> getAlbumOne(@PathVariable String nickname,@PathVariable("albumId") int id){
        ResponseDto<SearchTotalResultDto> responseDto = adminService.getAlbumOne(nickname,id);

        return ResponseEntity.ok().body(responseDto);
    }
}

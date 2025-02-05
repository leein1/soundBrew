package com.soundbrew.soundbrew.controller;


import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.service.AdminService;
import com.soundbrew.soundbrew.service.AdminServiceImpl;
import com.soundbrew.soundbrew.service.user.UserService;
import com.soundbrew.soundbrew.service.SoundsService;
import com.soundbrew.soundbrew.service.TagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@AllArgsConstructor
@RequiredArgsConstructor
@Log4j2
public class AdminController {  //  관리자용 컨트롤러
    /*
        1. 관리자 자격 확인 과정 필요
        2. 관리자일 경우 필터링 각 메서드 마다 달리 적용 필요
     */

    private final AdminService adminService;
    private final AdminServiceImpl adminServiceImpl;
    private final UserService userService;
    private final SoundsService soundsService;
    private final TagsService tagsService;

    //  모든 유저 조회
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO<UserDetailsDTO>> getAllUsers(@RequestParam RequestDTO requestDTO) {

        ResponseDTO responseDTO = userService.getAllUserWithDetails(requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<UserDetailsDTO>> getUser(@PathVariable int userId) {

       ResponseDTO responseDTO = userService.getUserWithDetails(userId);

       return ResponseEntity.ok().body(responseDTO);
    }

    // 업데이트 방식 어떻게??
    @PatchMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<UserDetailsDTO>> updateUser(@PathVariable int userId, @RequestBody UserDetailsDTO userDetailsDTO) {

        return null;
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable int userId) {

        userService.deleteUser(userId);

        return null;

    }

    @PostMapping("/tags")
    ResponseEntity<ResponseDTO> createTag(@RequestBody TagsDTO tagsDto){
        ResponseDTO responseDto = tagsService.createTag(tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/instruments/{beforeName}")
    ResponseEntity<ResponseDTO> updateInstrumentTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
        ResponseDTO responseDto  = tagsService.updateInstrumentTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/moods/{beforeName}")
    ResponseEntity<ResponseDTO> updateMoodTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
        ResponseDTO responseDto = tagsService.updateMoodTagSpelling(beforeName, afterName);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/genres/{beforeName}")
    ResponseEntity<ResponseDTO> updateGenreTagSpelling(@PathVariable String beforeName, @RequestBody String afterName){
            ResponseDTO responseDto = tagsService.updateGenreTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tags/{musicId}")
    ResponseEntity<ResponseDTO> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDTO tagsDto){
        ResponseDTO responseDto = tagsService.updateLinkTags(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // sounds for admin
    @DeleteMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> deleteAlbum(@PathVariable int albumId){
        ResponseDTO responseDto = soundsService.deleteAlbum(albumId);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> deleteMusic(@PathVariable int musicId){
        ResponseDTO responseDto = soundsService.deleteMusic(musicId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDTO albumDto){
        ResponseDTO responseDto = soundsService.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}/verify")
    ResponseEntity<ResponseDTO> updateVerifyAlbum(@PathVariable int albumId){
        ResponseDTO responseDTO = soundsService.updateVerifyAlbum(albumId);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/albums/verify")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> readVerifyAlbum(RequestDTO requestDTO){
        ResponseDTO<SearchTotalResultDTO> responseDTO = soundsService.readVerifyAlbum(requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/albums/{userId}/title/{albumId}/verify")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> readVerifyAlbumOne(@PathVariable("userId") int userId, @PathVariable("albumId") int albumId, RequestDTO requestDTO){
        ResponseDTO<SearchTotalResultDTO> responseDTO = soundsService.readVerifyAlbumOne(userId,albumId,requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> updateMusic(@PathVariable int musicId, @RequestBody MusicDTO musicDto ){
        ResponseDTO responseDto = soundsService.updateMusic(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{userId}/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable int userId, @PathVariable("musicId") int id){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOneForAdmin(userId,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{userId}/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable int userId, @PathVariable("albumId") int id, RequestDTO requestDTO){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOneForAdmin(userId,id,requestDTO);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getTracks(@ModelAttribute RequestDTO requestDTO ){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDTO);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbums(@ModelAttribute RequestDTO requestDTO){
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumMe(requestDTO);

        return  ResponseEntity.ok().body(responseDto);
    }
}

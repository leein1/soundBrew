package com.soundbrew.soundbrew.controller;


import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.service.*;
import com.soundbrew.soundbrew.service.authentication.AuthenticationService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/admin")
//@AllArgsConstructor
@RequiredArgsConstructor
@Log4j2
@Validated
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
    private final AuthenticationService authenticationService;

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
    ResponseEntity<ResponseDTO> createTag(@RequestBody @Valid TagsDTO tagsDto, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = tagsService.createTag(tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/instruments/{beforeName}")
    ResponseEntity<ResponseDTO> updateInstrumentTagSpelling(
            @PathVariable
            @Size(min = 2, max = 50)
            @Pattern(regexp = "^[a-z0-9\\s]*$")
            String beforeName,

            @RequestBody
            @Valid
            TagsDTO afterName,

            Authentication authentication
    ){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto  = tagsService.updateInstrumentTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/moods/{beforeName}")
    ResponseEntity<ResponseDTO> updateMoodTagSpelling(
            @PathVariable
            @Size(min = 2, max = 50)
            @Pattern(regexp = "^[a-z0-9\\s]*$")
            String beforeName,

            @RequestBody
            @Valid
            TagsDTO afterName,

            Authentication authentication
    ){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = tagsService.updateMoodTagSpelling(beforeName, afterName);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tags/genres/{beforeName}")
    ResponseEntity<ResponseDTO> updateGenreTagSpelling(
            @PathVariable
            @Size(min = 2, max = 50)
            @Pattern(regexp = "^[a-z0-9\\s]*$")
            String beforeName,

            @RequestBody
            @Valid
            TagsDTO afterName,

            Authentication authentication
    ){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = tagsService.updateGenreTagSpelling(beforeName, afterName);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tags/{musicId}")
    ResponseEntity<ResponseDTO> updateLinkTags(@PathVariable @Positive int musicId, @RequestBody @Valid TagsDTO tagsDto, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = tagsService.updateLinkTagsForAdmin(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // sounds for admin
    @DeleteMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> deleteAlbum(@PathVariable @Positive int albumId, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = soundsService.deleteAlbum(albumId);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> deleteMusic(@PathVariable @Positive int musicId, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = soundsService.deleteMusic(musicId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> updateAlbum(@PathVariable @Positive int albumId, @RequestBody @Valid AlbumDTO albumDto, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = soundsService.updateAlbumForAdmin(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}/verify")
    ResponseEntity<ResponseDTO> updateVerifyAlbum(@PathVariable @Positive int albumId, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDTO = soundsService.updateVerifyAlbum(albumId);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/albums/verify")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> readVerifyAlbum( @Valid RequestDTO requestDTO, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO<SearchTotalResultDTO> responseDTO = soundsService.readVerifyAlbum(requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/albums/{userId}/title/{albumId}/verify")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> readVerifyAlbumOne(@PathVariable("userId") @Positive int userId, @PathVariable("albumId") @Positive int albumId, @Valid RequestDTO requestDTO, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO<SearchTotalResultDTO> responseDTO = soundsService.readVerifyAlbumOne(userId,albumId,requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> updateMusic(@PathVariable @Positive int musicId, @RequestBody @Valid MusicDTO musicDto, Authentication authentication ){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO responseDto = soundsService.updateMusicForAdmin(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{userId}/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable @Positive int userId, @PathVariable("musicId") @Positive int id, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOneForAdmin(userId,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{userId}/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable @Positive int userId, @PathVariable("albumId") @Positive int id, @Valid RequestDTO requestDTO, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOneForAdmin(userId,id,requestDTO);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getTracks(@ModelAttribute @Valid RequestDTO requestDTO, Authentication authentication ){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDTO);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbums(@ModelAttribute @Valid RequestDTO requestDTO, Authentication authentication){
        if(!authenticationService.isAdmin(authentication)) throw new AccessDeniedException("해당 기능에 접근할 권한이 없습니다");

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumMe(requestDTO);

        return  ResponseEntity.ok().body(responseDto);
    }
}

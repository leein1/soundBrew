package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.SubscriptionDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.service.AuthenticationService;
import com.soundbrew.soundbrew.service.SoundsService;
import com.soundbrew.soundbrew.service.TagsService;
import com.soundbrew.soundbrew.service.subscription.SubscriptionService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
//@AllArgsConstructor
@Log4j2
public class MeController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final SoundsService soundsService;
    private final TagsService tagsService;
    private final AuthenticationService authenticationService;


//    내 정보 보기 - GET /me/{userId}
//    @ApiOperation(value = "me GET",notes = "GET 방식으로 내 정보 조회")
    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO<UserDTO>> getMe(Authentication authentication) {

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<UserDTO> responseDTO = userService.getUser(userId);

        log.info("******************************************" + responseDTO.toString());

        return ResponseEntity.ok().body(responseDTO);
    }


//    내 정보 수정 - PATCH /me/{userId}
//    수정 하는 방식으로 현재는 UserDTO 를 대입하지만 Map(k,v)로 바꾸는게 좋아보임
//    controller - Map으로 클라이언트측에서 수정한 값 가져오기,
//    service - 수정한 값 null 검증 후 수정
//    @ApiOperation(value = "me PATCH",notes = "PATCH 방식으로 내 정보 수정")
    @PatchMapping(value = "")
    public ResponseEntity<ResponseDTO<String>> updateMe(@RequestBody UserDTO userDTO){  //추후 토큰으로 변경

        ResponseDTO<String> responseDTO = userService.updateUser(userDTO);


        return ResponseEntity.ok().body(responseDTO);

    }


//    탈퇴 - DELETE /me/{userId}
//    비밀번호 확인 후 탈퇴 가능
//    @ApiOperation(value = "me DELETE", notes = "DELETE 탈퇴")
    @DeleteMapping("")
    public ResponseEntity<ResponseDTO<String>> deleteMe(Authentication authentication){ //추후 토큰으로 변경

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<String> responseDTO = userService.deleteUser(userId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    회원 구독제 보기 GET /me/subscription
//    반환형으로 SubscriptionDTO ? UserSubscriptionDTO
//    @ApiOperation(value = "subscription GET", notes = "GET 내 구독제 정보 가져오기")
    @GetMapping("/subscription")
    public ResponseEntity<ResponseDTO<?>> getSubscriptionInfo(Authentication authentication){

        int userId = authenticationService.getUserId(authentication);

        // user 조회
        UserDTO userDTO = userService.getUser(userId).getDto();

        // user가 구독중인 subscriptionId 가져오기
        int subscriptionId = userDTO.getSubscriptionId();

        // subscriptionId로 구독제 정보 가져오기
        try {

            ResponseDTO<SubscriptionDTO> responseDTO = subscriptionService.getSubscription(subscriptionId);

            return ResponseEntity.ok().body(responseDTO);

        } catch (NoSuchElementException e) {

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("구독중인 구독제가 없습니다.").build();

            return ResponseEntity.ok().body(responseDTO);
        }


    }


//    회원 구독제 등록 POST /me/subscription/{subscriptionId}
//    @ApiOperation(value = "subscription POST", notes = "POST 유저의 구독제 구독")
    @PostMapping("/subscription/{subscriptionId}")
    public ResponseEntity<ResponseDTO<String>> addSubscription(@PathVariable int subscriptionId, Authentication authentication){ //추후 토큰으로 변경

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<String> responseDTO = userService.addUserSubscription(userId, subscriptionId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    구독제 정보 수정 PATCH /me/subscription
//    @ApiOperation(value = "subscription PATCH", notes = "PATCH 유저의 구독제 변경")
    @PatchMapping("/subscrion/{subscriptionId}")
    public ResponseEntity<ResponseDTO<String>> updateSubscription(@PathVariable int subscriptionId, Authentication authentication){

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<String> responseDTO = userService.updateUserSubscription(subscriptionId, userId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    구독제 정보 삭제 DELETE /me/subscription
//    @ApiOperation(value = "subscription DELETE", notes = "DELETE 유저의 구독 취소")
    @DeleteMapping("/subscription")
    public ResponseEntity<ResponseDTO<String>> cancleSubscription(Authentication authentication){

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<String> responseDTO = userService.deleteUserSubscription(userId);

        return ResponseEntity.ok().body(responseDTO);

    }



    @PostMapping("/tracks/{musicId}/tags")
    ResponseEntity<ResponseDTO> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDTO tagsDto){
//        Authentication authentication
//        int userId = authenticationService.getUserId(authentication);

        ResponseDTO responseDto = tagsService.updateLinkTags(musicId,tagsDto);
    }


    // sounds for me
    @PostMapping("/sounds")
    ResponseEntity<ResponseDTO> createSound(@RequestBody SoundCreateDTO soundCreateDto){
//        Authentication authentication
//        int userId = authenticationService.getUserId(authentication);

        AlbumDTO albumDto = soundCreateDto.getAlbumDTO();
        MusicDTO musicDto = soundCreateDto.getMusicDTO();
        TagsDTO tagsDto = soundCreateDto.getTagsDTO();

        ResponseDTO responseDto = soundsService.createSound(2,albumDto,musicDto,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDTO albumDto){
        //        Authentication authentication
//        int userId = authenticationService.getUserId(authentication);

        ResponseDTO responseDto = soundsService.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> updateMusic(@PathVariable int musicId, @RequestBody MusicDTO musicDto ){
        //        Authentication authentication
//        int userId = authenticationService.getUserId(authentication);

        ResponseDTO responseDto = soundsService.updateMusic(musicId,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable("musicId") int id){
        //        Authentication authentication
//        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOne(2,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable("albumId") int id, RequestDTO requestDto){
        //        Authentication authentication
//        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOne(2,id,requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundMe(RequestDTO requestDto){
        //        Authentication authentication
//        String nickname = authenticationService.getNickname(authentication);

        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

//    @GetMapping("/tags")
//    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getTagsMe(RequestDTO requestDto){
//        //Authentication authentication
//        //String nickname = authenticationService.getNickname(authentication);
//
//        requestDto.setKeyword("u_1");
//        requestDto.setType("n");
//        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDto);
//
//        return ResponseEntity.ok().body(responseDto);
//    }

    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumMe(RequestDTO requestDto){
//        Authentication authentication
//        String nickname = authenticationService.getNickname(authentication);
        requestDto.setKeyword("u_1");

        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumMe(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable("albumId") int id, RequestDTO requestDto){
        //쿠키 -> 내이름
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOne(2,id,requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDTO albumDto){

        ResponseDTO responseDto = soundsService.updateAlbum(albumId,albumDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundMe(RequestDTO requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable("musicId") int id, Authentication authentication){

        int userId = authenticationService.getUserId(authentication);
        // 쿠키 -> 내이름
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOne(userId,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/tracks/{musicId}/tags")
    ResponseEntity<ResponseDTO> updateLinkTags(@PathVariable int musicId, @RequestBody TagsDTO tagsDto){

        ResponseDTO responseDto = tagsService.updateLinkTags(musicId,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO> updateMusic(@PathVariable int musicId, @RequestBody MusicDTO musicDto ){

        ResponseDTO responseDto = soundsService.updateMusic(2,musicDto);

        return ResponseEntity.ok().body(responseDto);
    }


    @GetMapping("/tags")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getTagsMe(RequestDTO requestDto){
        requestDto.setKeyword("u_1");
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }




}

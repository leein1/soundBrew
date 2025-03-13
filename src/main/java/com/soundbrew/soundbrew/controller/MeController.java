package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.SubscriptionDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.handler.BusinessException;
import com.soundbrew.soundbrew.service.authentication.AuthenticationService;
import com.soundbrew.soundbrew.service.sound.SoundsService;
import com.soundbrew.soundbrew.service.tag.TagsService;
import com.soundbrew.soundbrew.service.subscription.SubscriptionService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
//@AllArgsConstructor
@Log4j2
@Validated
public class MeController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final SoundsService soundsService;
    private final TagsService tagsService;
    private final AuthenticationService authenticationService;

    //    내 정보 보기 - GET /me/{userId}
//    @ApiOperation(value = "me GET",notes = "GET 방식으로 내 정보 조회")
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO<?>> getMe(Authentication authentication) {

        int userId = authenticationService.getUserId(authentication);

        if(!authenticationService.isUser(authentication)){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ResponseDTO.<String>withMessage().message("수정 권한이 없습니다.").build()
            );
        }



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
    public ResponseEntity<ResponseDTO<String>> updateMe(@RequestBody UserDTO userDTO, Authentication authentication){

        int authenticatedUserId = authenticationService.getUserId(authentication);

        if (authenticatedUserId != userDTO.getUserId() || !authenticationService.isUser(authentication)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ResponseDTO.<String>withMessage().message("수정 권한이 없습니다.").build()
            );
        }

        log.info("요청된 업데이트 데이터: {}", userDTO);

        ResponseDTO<String> responseDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(responseDTO);
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

    @PatchMapping("/password")
    public ResponseEntity<ResponseDTO<String>> changePassword(Authentication authentication,@RequestBody UserDTO userDTO){

        if(!authenticationService.isUser(authentication)){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ResponseDTO.<String>withMessage().message("수정 권한이 없습니다.").build()
            );
        }

        int userId = authenticationService.getUserId(authentication);
        userDTO.setUserId(userId);

        ResponseDTO responseDTO = userService.updatePassword(userDTO);

        return ResponseEntity.ok().body(responseDTO);
    }


    //    회원 구독제 보기 GET /me/subscription
//    반환형으로 SubscriptionDTO ? UserSubscriptionDTO
//    @ApiOperation(value = "subscription GET", notes = "GET 내 구독제 정보 가져오기")
    @GetMapping("/subscription")
    public ResponseEntity<ResponseDTO<SubscriptionDTO>> getSubscriptionInfo(Authentication authentication){

        int userId = authenticationService.getUserId(authentication);

        // user 조회
        UserDTO userDTO = userService.getUser(userId).getDto();

        // user가 구독중인 subscriptionId 가져오기
        int subscriptionId = userDTO.getSubscriptionId();

        // subscriptionId로 구독제 정보 가져오기
        ResponseDTO<SubscriptionDTO> responseDTO = subscriptionService.getSubscription(subscriptionId);

        return ResponseEntity.ok().body(responseDTO);


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

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/tracks/{musicId}/tags")
    ResponseEntity<ResponseDTO<String>> updateLinkTags(@PathVariable @Positive int musicId, @RequestBody  TagsDTO tagsDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO responseDto = tagsService.updateLinkTagsForArtist(musicId,tagsDto,userId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/sounds")
    ResponseEntity<ResponseDTO<String>> createSound(@RequestBody SoundCreateDTO soundCreateDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        int userId = authenticationService.getUserId(authentication);

        AlbumDTO albumDto = soundCreateDto.getAlbumDTO();
        MusicDTO musicDto = soundCreateDto.getMusicDTO();
        TagsDTO tagsDto = soundCreateDto.getTagsDTO();

        ResponseDTO responseDto = soundsService.createSound(userId,albumDto,musicDto,tagsDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO<String>> updateAlbum(@PathVariable @Positive int albumId, @RequestBody AlbumDTO albumDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO responseDto = soundsService.updateAlbumForArtist(albumId,albumDto,userId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO<String>> updateMusic(@PathVariable @Positive int musicId, @RequestBody MusicDTO musicDto, Authentication authentication ){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO responseDto = soundsService.updateMusicForArtist(musicId,musicDto,userId);

        return ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tracks/{musicId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundOne(@PathVariable("musicId") @Positive int id, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundOne(userId,id);

        return ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/albums/{albumId}")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumOne(@PathVariable("albumId") @Positive int id,  @Valid RequestDTO requestDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        int userId = authenticationService.getUserId(authentication);

        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumOne(userId,id,requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundMe(@Valid RequestDTO requestDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        String nickname = authenticationService.getNickname(authentication);

        requestDto.setKeyword(nickname);
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/albums")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getAlbumMe( @Valid RequestDTO requestDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        String nickname = authenticationService.getNickname(authentication);

        requestDto.setKeyword(nickname);
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getAlbumMe(requestDto);

        return  ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tags")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getTagsMe(  @Valid RequestDTO requestDto, Authentication authentication){
        if(!authenticationService.isUser(authentication))throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);

        String nickname = authenticationService.getNickname(authentication);

        requestDto.setKeyword(nickname);
        requestDto.setType("n");
        ResponseDTO<SearchTotalResultDTO> responseDto = soundsService.getSoundMe(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/albums/{albumId}/tracks")
    ResponseEntity<ResponseDTO<SearchTotalResultDTO>> getSoundsByAlbumId(@PathVariable @Positive int albumId,Authentication authentication){
//        if(!authenticationService.isAdmin(authentication)) throw new ResourceOwnershipException("해당 기능에 접근할 권한이 없습니다");
//        int userId = authenticationService.getUserId(authentication);
        ResponseDTO<SearchTotalResultDTO> responseDTO = soundsService.getSoundsByAlbumId(2,albumId);

        return ResponseEntity.ok().body(responseDTO);
    }
}
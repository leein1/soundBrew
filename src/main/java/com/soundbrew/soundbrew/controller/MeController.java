package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.SubscriptionDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.MeService;
import com.soundbrew.soundbrew.service.subscription.SubscriptionService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
//@AllArgsConstructor
@Log4j2
public class MeController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final MeService meService;


//    @GetMapping("/list")
//    public ResponseDTO<UserDTO> list() {
//
//        ResponseDTO<UserDTO> responseDTO = userService.getAllUsers();
//
////        확인용
//        if (!responseDTO.isHasContent()) {
//            log.info("No users found.");
////            status등 반환 해줘야함
//        } else {
//
//            log.info("Users found: " + responseDTO.getDtoList().size());
//            //            status등 반환 해줘야함
//
//        }
//
//        //            status등 반환 해줘야함
//
//        return responseDTO;
//
//    }


//  해당 기능은 AdminController로 이동

//    // 서비스 코드 미완성!!!!
//    @ApiOperation(value = "users GET", notes = "GET 방식으로 유저 리스트 조회")
//    @GetMapping(value = "/")
//    public ResponseEntity<ResponseDTO<UserDTO>> getUserList(@ModelAttribute RequestDTO requestDTO) {
//
//        ResponseDTO<UserDTO> responseDTO = userService.list(requestDTO);
//
//        log.info("UserController list() : {}", responseDTO);
//
//
//        return ResponseEntity.ok(responseDTO);
//    }

//    @PostMapping("/register")
//    public ResponseEntity registerUser(@RequestBody UserDTO userDTO) {
//
////        try{
////            boolean result = userService.registerUser(userDTO);
////
////            //  비밀번호 실패 처리
////            if(result == false){
////                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
////            }
////
////            //  회원가입 성공
////            return ResponseEntity.status(HttpStatus.OK).body(result);
////        } catch (IllegalArgumentException e){
////            //  무결성 위배 등 기타 오류
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
////        } catch (RuntimeException e){
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
////        }
//
////        반환형 변경으로 수정
////        ApiResponse response = userService.registerUser(userDTO);
////
////        return ResponseEntity.status(response.getStatus()).body(response);
//
////        Exception핸들러 사용으로 수정
//        userService.registerUser(userDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//
//    }



//    해당 기능은 UsersController로 이동
//    @ApiOperation(value = "users POST",notes = "POST 방식으로 회원 등록")
//    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseDTO<Object>> resgister(@RequestBody UserDTO userDTO) {
//
//        userService.registerUser(userDTO);
//
//        ResponseDTO responseDTO = ResponseDTO.withMessage()
//                .message("회원가입에 성공 하였습니다.")
//                .build();
//
//        return ResponseEntity.ok(responseDTO);
//
//    }



//    @ApiOperation(value = "리퀘스트 테스트",notes = "more 필드 받는거확인")




//    검색을 nickname으로 해야 하는가 userId로 해야 하는가

//    내 정보 보기 - GET /me/{userId}
//    @ApiOperation(value = "me GET",notes = "GET 방식으로 내 정보 조회")
    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO<UserDTO>> getMe() {   //추후 토큰에서 user id 추출 해야 함

            ResponseDTO<UserDTO> responseDTO = userService.getUser(16);

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
    public ResponseEntity<ResponseDTO<String>> deleteMe(@RequestParam int userId){ //추후 토큰으로 변경

        ResponseDTO<String> responseDTO = userService.deleteUser(userId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    회원 구독제 보기 GET /me/subscription
//    반환형으로 SubscriptionDTO ? UserSubscriptionDTO
//    @ApiOperation(value = "subscription GET", notes = "GET 내 구독제 정보 가져오기")
    @GetMapping("/subscription")
    public ResponseEntity<ResponseDTO<?>> getSubscriptionInfo(@RequestParam("userId") int userId){ //추후 토큰으로 변경

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
    public ResponseEntity<ResponseDTO<String>> addSubscription(@PathVariable int subscriptionId, @RequestParam int userId){ //추후 토큰으로 변경

       ResponseDTO<String> responseDTO = userService.addUserSubscription(userId, subscriptionId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    구독제 정보 수정 PATCH /me/subscription
//    @ApiOperation(value = "subscription PATCH", notes = "PATCH 유저의 구독제 변경")
    @PatchMapping("/subscrion/{subscriptionId}")
    public ResponseEntity<ResponseDTO<String>> updateSubscription(@PathVariable int subscriptionId, @RequestParam int userId){

        ResponseDTO<String> responseDTO = userService.updateUserSubscription(subscriptionId, userId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    구독제 정보 삭제 DELETE /me/subscription
//    @ApiOperation(value = "subscription DELETE", notes = "DELETE 유저의 구독 취소")
    @DeleteMapping("/subscription")
    public ResponseEntity<ResponseDTO<String>> cancleSubscription(int userId){

        ResponseDTO<String> responseDTO = userService.deleteUserSubscription(userId);

        return ResponseEntity.ok().body(responseDTO);

    }



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

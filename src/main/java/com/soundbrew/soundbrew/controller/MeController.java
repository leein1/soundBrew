package com.soundbrew.soundbrew.controller;


import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.SubscriptionDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.service.SubscriptionService;
import com.soundbrew.soundbrew.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@Log4j2
public class MeController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;


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
    @GetMapping("/test")
    public ResponseEntity<ResponseDTO> test(@ModelAttribute RequestDTO requestDTO){

        return null;
    }




//    검색을 nickname으로 해야 하는가 userId로 해야 하는가

//    내 정보 보기 - GET /me/{userId}
//    @ApiOperation(value = "me GET",notes = "GET 방식으로 내 정보 조회")
    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO<UserDTO>> getMe(@RequestParam("nickname") String nickname) {   //추후 토큰에서 user nickname 추출 해야 함

        ResponseDTO<UserDTO> responseDTO = userService.getUserByNickname(nickname);

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
    public ResponseEntity<ResponseDTO<String>> deleteMe(@RequestParam String nickname){ //추후 토큰으로 변경

        ResponseDTO<String> responseDTO = userService.deleteUserByNickname(nickname);

        return ResponseEntity.ok().body(responseDTO);
    }


//    회원 구독제 보기 GET /me/subscription
//    반환형으로 SubscriptionDTO ? UserSubscriptionDTO
//    @ApiOperation(value = "subscription GET", notes = "GET 내 구독제 정보 가져오기")
    @GetMapping("/subscription")
    public ResponseEntity<ResponseDTO<SubscriptionDTO>> getSubscription(@RequestParam("nickname") String nickname){ //추후 토큰으로 변경

        // user 조회
        UserDTO userDTO = userService.getUserByNickname(nickname).getDto();

        // user가 구독중인 subscriptionId 가져오기
        int subscriptionId = userDTO.getSubscriptionId();

        // subscriptionId로 구독제 정보 가져오기
        ResponseDTO<SubscriptionDTO> responseDTO = subscriptionService.findOneSubscription(subscriptionId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    회원 구독제 등록 POST /me/subscription/{subscriptionId}
//    @ApiOperation(value = "subscription POST", notes = "POST 유저의 구독제 구독")
    @PostMapping("/subscription/{subscriptionId}")
    public ResponseEntity addSubscription(@PathVariable int subscriptionId, @RequestParam int userId){ //추후 토큰으로 변경

       ResponseDTO<String> responseDTO = subscriptionService.addUserSubscription(userId, subscriptionId);

        return ResponseEntity.ok().body(responseDTO);
    }


//    구독제 정보 수정 PATCH /me/subscription
//    @ApiOperation(value = "subscription PATCH", notes = "PATCH 유저의 구독제 변경")
    @PatchMapping("/subscription/{subscriptionId}")
    public ResponseEntity updateSubscription(@PathVariable int subscriptionId,@RequestParam int userId){    //추후 토큰으로 변경





        return null;
    }


//    구독제 정보 삭제 DELETE /me/subscription
//    @ApiOperation(value = "subscription DELETE", notes = "DELETE 유저의 구독 취소")
    @DeleteMapping("/subscription")
    public ResponseEntity deleteSubscription(@RequestParam int subscriptionId, @RequestParam int userId) {  //추후 토큰으로 변경



        return null;
    }



//    구매기록보기
//    구매하기
//    다운로드 기록 보기



//    나의 모든 앨범 - GET /me/albums
    @GetMapping("/albums")
    public ResponseEntity getMyAlbums(@RequestParam int userId){    //추후 토큰으로 변경

        //내 앨범 요청
        return null;
    }


//    나의 특정 앨범 - GET /me/albums/{albumId}
//    메서드 이름 수정 필요
    @GetMapping("/albums/{albumId}")
    public ResponseEntity getMyAlbums(@PathVariable int albumId, @RequestParam int userId){    //추후 토큰으로 변경

        return null;
    }



//    나의 특정 앨범수정 - PATCH /me/albums/{albumId} - 해당 행위를 /api/me에서 처리하는것이 맞는가?
//    userid를 토큰에서 받아옴 -

    



//    나의 모든 음원 - GET /me/tracks
    @GetMapping("/tracks")
    public ResponseEntity getMyTracks(@RequestParam String nickname){    //추후 토큰으로 변경

        //내 앨범 요청
        return null;
    }


//    나의 특정 음원 - GET /me/tracks/{trackId}
//    메서드 이름 수정 필요
    @GetMapping("/tracks/{musicId}")
    public ResponseEntity getMyTracks(@PathVariable int musicId, @RequestParam String nickname){    //추후 토큰으로 변경

        //내 앨범 요청
        return null;
    }



//   나의 특정 음원 수정 - PATCH /me/tracks/{trackId} - 해당 행위를 /api/me에서 처리하는것이 맞는가?


}
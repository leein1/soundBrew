package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
public class UsersController {

    private final UserService userService;


//    @ApiOperation(value = "users POST", notes = "POST 방식으로 회원 등록")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<String>> resgister(@RequestBody UserDTO userDTO) {

        log.info("회원가입 요청 --------------------------------");

        try {
            userService.registerUser(userDTO);

            ResponseDTO responseDTO = ResponseDTO.withMessage()
                    .message("회원가입에 성공 하였습니다.")
                    .build();

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {

            throw new RuntimeException("회원가입 중 예상치 못한 문제가 발생하였습니다. 잠시후 다시 시도 해주세요");
        }

    }



//    //회원의 DB의 프로필 컬럼에 값 업데이트
//    @PatchMapping("/profiles/{userId}/{fileName}")
//    public ResponseEntity<ResponseDTO<String>> updateProfile(@PathVariable int userId, @PathVariable String fileName){
//        log.info("프로필 업데이트 요청 -----------------------------");
//
//        try{
//            ResponseDTO<String> responseDTO =userService.updateProfile(userId,fileName);
//            return ResponseEntity.ok().body(responseDTO);
//        }catch (Exception e){
//            throw new RuntimeException("프로필 업데이트 중 예상치 못한 문제가 발생했습니다.");
//        }
//    }





}

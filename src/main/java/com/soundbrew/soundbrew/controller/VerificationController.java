package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ActivationCodeDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import com.soundbrew.soundbrew.service.verification.ActivationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
@Log4j2
public class VerificationController {

    /*
        메일 전송 및 인증 과정 컨트롤러 위치 논의 필요
            - api를 따로 발급 해야 하는가
            - 발급하지 않는다면 어느 api 하위 목록이어야 하는가
     */

//    private final UserService userService;


    private final ActivationCodeService activationCodeService;
    private final UserService userService;

    //  유저 활성화
    @PostMapping("/activation")
    public ResponseEntity<ResponseDTO<String>> activationUser(@RequestBody ActivationCodeDTO activationCodeDTO){

        String activationCode = activationCodeDTO.getActivationCode();

        log.info("Activation code is {}", activationCode);

        ResponseDTO<String> responseDTO = activationCodeService.activateUser(activationCode);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/email")
    public ResponseEntity<Map<String,Boolean>> checkEmail(@RequestBody UserDTO user){

        String email = user.getEmail();

        //  조회 후 결과 있으면 false반환
        if(userService.isEmailExist(email)){

            return ResponseEntity.ok().body(Map.of("available", false));

        }else {

            return ResponseEntity.ok().body(Map.of("available", true));
        }
    }

    @PostMapping("/nickname")
    public ResponseEntity<Map<String,Boolean>> checkNickname(@RequestBody UserDTO user){

        String nickname = user.getNickname();

        //  조회 후 결과 있으면 false반환
        if(userService.isNicknameExist(nickname)){

            return ResponseEntity.ok().body(Map.of("available", false));

        }else {

            return ResponseEntity.ok().body(Map.of("available", true));
        }
    }


//    비밀번호 확인 - POST /me/password
    @PostMapping("/password")
    public ResponseEntity<ResponseDTO> verifyPassword(@RequestBody int userId, @RequestBody String providedPassword){

        ResponseDTO<String> responseDTO = userService.verifyPassword(userId, providedPassword);

        return ResponseEntity.ok().body(responseDTO);
    }

    // 비밀번호 분실시
//    @PatchMapping("/password")
//    public ResponseEntity<ResponseDTO> changePassword(@RequestBody int userId, @RequestBody String newPassword){
//
//        // 입력받은 비밀번호가 없다면 자체적으로 비밀번호 reset
//        // 입력받은 비밀번호가 있다면 입력받은것으로 변경
//        return null;
//    }


}

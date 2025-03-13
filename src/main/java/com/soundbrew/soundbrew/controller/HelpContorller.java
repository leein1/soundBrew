package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.service.authentication.AuthenticationService;
import com.soundbrew.soundbrew.service.mail.MailService;
import com.soundbrew.soundbrew.service.user.UserService;
import com.soundbrew.soundbrew.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/help")
//@AllArgsConstructor
@RequiredArgsConstructor
@Log4j2
public class HelpContorller {

    private final UserService userService;
    private final AuthenticationService authenticationService;



    @PostMapping("/find-password")
    public ResponseEntity<ResponseDTO<String>> tempPassword(@RequestBody UserDTO userDTO){

        log.info("---------------------------/api/help/find-password" + userDTO.toString());

        ResponseDTO responseDTO = userService.generateTemporaryPassword(userDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO<String>> resetPassword(@RequestBody UserDTO userDTO, Authentication authentication){

        log.info("----------------------------------------HelpContorller.resetPassword" + userDTO.toString());

        // Authentication 의 authorities에 PASSWORD_RESET이 없는 경우 -
        boolean isPasswordReset = authenticationService.isPasswordReset(authentication);

        if(!isPasswordReset){

            log.info("비밀번호 리셋 권한이 없음");

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage().message("오류가 발생했습니다.").build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);

        }

        // Authentication에서 이메일 가져오기
        String email = authenticationService.getEmail(authentication);

        userDTO.setEmail(email);

        ResponseDTO responseDTO = userService.updatePassword(userDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

}

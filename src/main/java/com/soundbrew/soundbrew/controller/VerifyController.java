package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/verify")
@RequiredArgsConstructor
@Log4j2
public class VerifyController {



    /*
        메일 전송 및 인증 과정 컨트롤러 위치 논의 필요
            - api를 따로 발급 해야 하는가
            - 발급하지 않는다면 어느 api 하위 목록이어야 하는가
     */

    private final UserService userService;

    @ApiOperation(value = "verify-password POST", notes = "POST 비밀번호 인증")
    @PostMapping("/password")
    ResponseEntity<ResponseDTO<String>> verifyPassword(@RequestParam String nickname, @RequestParam String password) {
        ResponseDTO<String> responseDTO = userService.verifyPassword(nickname, password);
        if (responseDTO.getMessage().equals("확인되었습니다.")) {

            // 토큰 또는 세션 데이터 생성 필요

            return ResponseEntity.ok(ResponseDTO.<String>withMessage()
                    .message("인증 성공")
                    .build());  // 토큰 또는 세션 정보 담아야 함
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseDTO);
    }
}

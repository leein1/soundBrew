package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
public class UsersController {

    private final UserService userService;

//    @ApiOperation(value = "users POST", notes = "POST 방식으로 회원 등록")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<Object>> resgister(@RequestBody UserDTO userDTO) {

        userService.registerUser(userDTO);

        ResponseDTO responseDTO = ResponseDTO.withMessage()
                .message("회원가입에 성공 하였습니다.")
                .build();

        return ResponseEntity.ok(responseDTO);

    }



}

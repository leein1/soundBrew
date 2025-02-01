package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
public class UsersController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsers() {

        return null;
    }


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





}

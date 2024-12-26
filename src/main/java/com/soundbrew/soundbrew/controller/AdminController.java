package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDetailsDTO;
import com.soundbrew.soundbrew.service.AdminService;
import com.soundbrew.soundbrew.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {  //  관리자용 컨트롤러
    /*
        1. 관리자 자격 확인 과정 필요
        2. 관리자일 경우 필터링 각 메서드 마다 달리 적용 필요
     */

    private final AdminService adminService;

    //  모든 유저 조회
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO<UserDetailsDTO>> getAllUsers(RequestDTO requestDTO) {

        ResponseDTO responseDTO = adminService.getAlluser(requestDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<UserDetailsDTO>> getUserById(@PathVariable int userId) {

       ResponseDTO responseDTO = adminService.getUser(userId);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<UserDetailsDTO>> updateUser(@PathVariable int userId, @RequestBody UserDetailsDTO userDetailsDTO) {
        return null;
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable int userId) {
        return null;
    }


}

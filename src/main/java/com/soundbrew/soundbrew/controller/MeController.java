package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.service.UserService;
import com.soundbrew.soundbrew.util.StringProcessorImpl;
import com.soundbrew.soundbrew.util.UserValidator;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@Log4j2
public class MeController {

    private final UserService userService;

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



}

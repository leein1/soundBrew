package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ApiResponse;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.service.UserService;
import com.soundbrew.soundbrew.util.StringProcessorImpl;
import com.soundbrew.soundbrew.util.UserValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseDTO<UserDTO> list(Model model) {

        ResponseDTO<UserDTO> responseDTO = userService.getAllUsers();

//        확인용
        if (!responseDTO.isHasContent()) {
            log.info("No users found.");
        } else {

            log.info("Users found: " + responseDTO.getDtoList().size());
        }

        return responseDTO;

    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserDTO userDTO) {

//        try{
//            boolean result = userService.registerUser(userDTO);
//
//            //  비밀번호 실패 처리
//            if(result == false){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
//            }
//
//            //  회원가입 성공
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        } catch (IllegalArgumentException e){
//            //  무결성 위배 등 기타 오류
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
//        } catch (RuntimeException e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
//        }

//        반환형 변경으로 수정
        ApiResponse response = userService.registerUser(userDTO);

        return ResponseEntity.status(response.getStatus()).body(response);

    }

}

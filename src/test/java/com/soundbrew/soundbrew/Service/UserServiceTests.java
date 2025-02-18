package com.soundbrew.soundbrew.Service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Transactional
    @Test
    public void testRegister(){
        UserDTO userDTO = UserDTO.builder()
                .name("donghun")
                .nickname("donghun")
                .password("testTEST123!@#")
                .phoneNumber("01012341234")
                .email("ddjsjs12@naver.com")
                .build();



        log.info(userService.registerUser(userDTO).toString());
    }

    @Transactional
    @Test
    public void testGetAllUserWithDetails(){
        // userId 2를 가진 요청 객체 생성
        RequestDTO requestDTO = RequestDTO.builder()
                .keyword("ddjsjs12")
                .type("n")
                .build();

        // 전체 유저정보 + 구독제 + 역할을 조회
        ResponseDTO<UserDetailsDTO> response = userService.getAllUserWithDetails(requestDTO);

        // 결과를 로그에 출력
        log.info("User details for userId 2: {}", response);
    }

}

package com.soundbrew.soundbrew.service;


import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void testGetAllUsers(){

        Optional<List<UserDTO>> userDTOs= userService.getAllUsers();

        if(userDTOs.isEmpty()){
            log.warn("리스트 비어 있음");

        } else{
            List<UserDTO> userDTOList = userDTOs.get();
            userDTOList.forEach(userDTO -> log.info(userDTO));
        }

    }

//    @Test
//    public void getUser(){
//
//        UserDTO userDTO = userService.getUser(16);
//
//        log.info("toString 출력 : {}", userDTO.toString());
//        log.info("생성일 출력 : {}", userDTO.getCreateDate());
//    }

    @Test
    public void tesGetUser(){

        int userId = 16;

        if(userService.getUser(userId).isEmpty()){

            log.info("사용자가 없음");
        } else{

            UserDTO userDTO = userService.getUser(userId).get();
            log.info(userDTO);
        }

    }

    @Test
    @Transactional
    public void testRegisterUser(){

        UserDTO userDTO = UserDTO.builder()
                .name("testName2")
                .nickname("testNickna2")
                .password("testPassword1@")
                .phonenumber("010-1234-1232")
                .email("test12342@test.com")
                .build();

        log.info(userService.registerUser(userDTO));
    }

    @Test
    public void testUpdatePassword(){

//        존재하는 유저
        int userId = 24;

//        존재하지 않는 유저
//        int userId = 100;

//        조건에 안 맞는 패스워드
//        String newPassword = "newPassword";

//조건에 맞는 패스워드
        String newPassword = "newPassword1!";

        log.info(userService.updatePassword(userId, newPassword));

    }


}


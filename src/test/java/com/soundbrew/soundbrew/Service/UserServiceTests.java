package com.soundbrew.soundbrew.Service;

import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;

//    @Test
//    public void testRegister(){
//        UserDTO userDTO = UserDTO.builder()
//                .name("test")
//                .nickname("test")
//                .password("testTEST123!@#")
//                .phoneNumber("01012341234")
//                .email("test@test.com")
//                .build();
//
//
//
//        log.info(userService.registerUser(userDTO).toString());
//    }
}

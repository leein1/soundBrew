package com.soundbrew.soundbrew.Service;

import com.soundbrew.soundbrew.dto.user.UserDTO;
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
                .name("leeinwon")
                .nickname("leeinwon")
                .password("testTEST123!@#")
                .phoneNumber("01012341234")
                .email("inwon.private@icloud.com")
                .build();



        log.info(userService.registerUser(userDTO).toString());
    }

}

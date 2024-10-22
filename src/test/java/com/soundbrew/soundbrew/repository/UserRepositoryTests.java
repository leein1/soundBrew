package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testInsert(){

        IntStream.rangeClosed(1,1).forEach(i ->{
            User user = User.builder()
                    .subscription_id(1)
                    .name("user_" + i)
                    .nickname("u_" + i)
                    .password("password_" + i)
                    .phonenumber("010-" + i)
                    .email(i + "_insert_test@test.com")
                    .build();

            User result = userRepository.save(user);

            log.info("user_id"+result.getUserId());
        });
    }

}

package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.ActivationCode;
import com.soundbrew.soundbrew.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
@Transactional
public class ActivationCodeRepositoryTests {

    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){

        user = User.builder()
                .subscriptionId(4)
                .name("user_" + 1234)
                .nickname("u_" + 1234)
                .password("password_" + 1234)
                .phonenumber("010-" + 1234)
                .email("activation_test@test.com")
                .build();
        userRepository.save(user);

        ActivationCode activationCode = ActivationCode.builder()
                .user(user)
                .activationCode("ABCD1234")
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .build();

        activationCodeRepository.save(activationCode);
    }

    @Test
    void findByUser_ActivationCode(){

        Optional<ActivationCode> result = activationCodeRepository.findByUser(user);

        assertThat(result).isPresent();
        assertThat(result.get().getActivationCode()).isEqualTo("ABCD1234");
        assertThat(result.get().getUser().getEmail()).isEqualTo("activation_test@test.com");
    }

    @Test
    void deleteByUser_ActivationCode(){

        activationCodeRepository.deleteByUser(user);

        Optional<ActivationCode> result = activationCodeRepository.findByUser(user);

        assertThat(result).isEmpty();
    }

}

package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.UserRole;
import com.soundbrew.soundbrew.domain.UserRoleId;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class UserRoleRepositoryTests {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void testInsert(){
        UserRoleId userRoleId = UserRoleId.builder()
                .role_id(3)
                .user_id(1)
                .build();

        UserRole userRole = UserRole.builder()
                .id(userRoleId)
                .build();

        UserRole result = userRoleRepository.save(userRole);
        log.info("user_role"+result.getId());

    }
}

package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.user.Role;
import com.soundbrew.soundbrew.repository.user.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class RoleRepositoryTests {

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testInsert(){
        Role role = Role.builder()
                .roleId(10)
                .roleType("USER")
                .build();

        Role result = roleRepository.save(role);

        log.info(result);
    }

    @Test
    public void testSelect(){

        int roleId = 3;

        Optional<Role> result = roleRepository.findById(roleId);

        Role role = result.orElseThrow();

        log.info(role);
    }

    @Transactional
    @Test
    public void testUpdate(){

        int roleId = 4;

        Optional<Role> result = roleRepository.findById(roleId);

        Role role = result.orElseThrow();

        role.change("UPDATE_TYPE");

        log.info(roleRepository.save(role));
    }

    @Test
    public void testDelete(){

        int roleId = 6;

        roleRepository.deleteById(roleId);

        Optional<Role> result = roleRepository.findById(roleId);

        if(result.isEmpty()){
            log.info("role_id : " + roleId + " has been deleted");
        } else {
            log.warn("role_id : " + roleId + " still exists");
        }
    }
}

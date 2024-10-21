package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Role;
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
                .role_id(10)
                .role_type("USER")
                .build();

        Role result = roleRepository.save(role);

        log.info(result);
    }

    @Test
    public void testSelect(){

        int role_id = 3;

        Optional<Role> result = roleRepository.findById(role_id);

        Role role = result.orElseThrow();

        log.info(role);
    }

    @Transactional
    @Test
    public void testUpdate(){

        int role_id = 4;

        Optional<Role> result = roleRepository.findById(role_id);

        Role role = result.orElseThrow();

        role.change("UPDATE_TYPE");

        log.info(roleRepository.save(role));
    }

    @Test
    public void testDelete(){

        int role_id = 6;

        roleRepository.deleteById(role_id);

        Optional<Role> result = roleRepository.findById(role_id);

        if(result.isEmpty()){
            log.info("role_id : " + role_id + " has been deleted");
        } else {
            log.warn("role_id : " + role_id + " still exists");
        }
    }
}

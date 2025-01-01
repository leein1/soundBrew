package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    public UserRepository userRepository;

    @Test
    public void testFindUserDetails(){


        UserDetailsDTO userDetailsDTO = userRepository.findUserDetailsById(16).orElseThrow();

        log.info(userDetailsDTO.toString());
    }

    @Test
    public void testFindUserDetailsByEmail(){

        UserDetailsDTO userDetailsDTO = userRepository.findUserDetailsByUsername("14_insert_test@test.com").orElseThrow();
        log.info(userDetailsDTO.toString());
    }


}


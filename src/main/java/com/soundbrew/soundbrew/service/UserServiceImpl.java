package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Override
    public Integer register(UserDTO userDTO) {


        return 0;
    }
}

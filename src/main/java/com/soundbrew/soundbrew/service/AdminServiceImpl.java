package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.dto.UserDetailsDTO;
import com.soundbrew.soundbrew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public ResponseDTO<UserDetailsDTO> getAlluser(RequestDTO requestDTO) {

        Optional<Page<UserDetailsDTO>> result = userRepository.findAllUserDetails(requestDTO);

        if(result.get().isEmpty()){
            List<UserDetailsDTO> userDetailsDTOList = Collections.emptyList();
            return ResponseDTO.<UserDetailsDTO>withAll(requestDTO, userDetailsDTOList,0);

        }

        return ResponseDTO.<UserDetailsDTO>withAll(requestDTO,result.get().getContent(),(int)result.get().getTotalElements());
    }

    @Override
    public ResponseDTO<UserDetailsDTO> getUser(int userId) {

        UserDetailsDTO userDetialsDTO = userRepository.findUserDetailsById(userId).orElseThrow();

        ResponseDTO<UserDetailsDTO> responseDTO = ResponseDTO.<UserDetailsDTO>withSingeData()
                .dto(userDetialsDTO)
                .build();

        return responseDTO;
    }








}

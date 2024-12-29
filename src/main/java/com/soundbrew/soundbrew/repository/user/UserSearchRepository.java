package com.soundbrew.soundbrew.repository.user;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserSearchRepository {

    public ResponseDTO<UserDTO> searchTest();

    public Optional<UserDetailsDTO> findUserDetailsById(int userId);

    public Optional<Page<UserDetailsDTO>> findAllUserDetails(RequestDTO requestDTO);
}

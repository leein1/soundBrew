package com.soundbrew.soundbrew.repository.search;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.dto.UserDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserSearchRepository {

    public ResponseDTO<UserDTO> searchTest();

    public Optional<UserDetailsDTO> findUserDetailsById(int userId);

    public Optional<Page<UserDetailsDTO>> findAllUserDetails(RequestDTO requestDTO);
}

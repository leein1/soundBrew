package com.soundbrew.soundbrew.repository.user.search;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserSearchRepository {

    Optional<UserDetailsDTO> findUserDetailsById(int userId);

    Optional<UserDetailsDTO> findUserDetailsByEmail(String email);

    Optional<Page<UserDetailsDTO>> findAllUserDetails(RequestDTO requestDTO);

}

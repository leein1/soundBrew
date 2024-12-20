package com.soundbrew.soundbrew.repository.search;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.dto.UserDetailsDTO;

public interface UserSearchRepository {

    public ResponseDTO<UserDTO> searchTest();

    public UserDetailsDTO findUserDetailsById(int userId);
}

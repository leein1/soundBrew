package com.soundbrew.soundbrew.repository.search;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;

public interface UserSearchRepository {

    public ResponseDTO<UserDTO> searchTest();
}

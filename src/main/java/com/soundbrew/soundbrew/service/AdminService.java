package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDetailsDTO;

import java.util.List;

public interface AdminService {

    public ResponseDTO<UserDetailsDTO> getAlluser(RequestDTO requestDTO);

    public ResponseDTO<UserDetailsDTO> getUser(int userId);
}

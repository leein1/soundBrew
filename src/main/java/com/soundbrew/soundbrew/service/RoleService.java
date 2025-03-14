package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.user.Role;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.RoleDTO;

public interface RoleService {

    String getRoleTypeById(int id);
}

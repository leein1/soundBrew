package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.user.Role;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.RoleDTO;
import com.soundbrew.soundbrew.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getRoleTypeById(int id) {

        Role role = roleRepository.findById(id).orElseThrow();

        return role.getRoleType();
    }
}

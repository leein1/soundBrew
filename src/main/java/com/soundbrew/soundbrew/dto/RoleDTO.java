package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.domain.Role;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private int roleId;

    private String roleType;

    public Role toEntity(){

        Role role = Role.builder()
                .roleId(this.roleId)
                .roleType(this.roleType)
                .build();

        return role;
    }

}

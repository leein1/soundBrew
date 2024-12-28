package com.soundbrew.soundbrew.dto.user;

import com.soundbrew.soundbrew.domain.user.Role;
import lombok.*;

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

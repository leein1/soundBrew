package com.soundbrew.soundbrew.dto.user;

import com.soundbrew.soundbrew.domain.user.UserRole;
import com.soundbrew.soundbrew.domain.user.UserRoleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDTO {

    private int roleId;

    private int userId;

    public UserRole toEntity(UserRoleDTO userRoleDTO){

        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(userRoleDTO.getRoleId())
                .userId(userRoleDTO.getUserId())
                .build();

        return UserRole.builder()
                .id(userRoleId)
                .build();
    }

}

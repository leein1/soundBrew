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

    public UserRole toEntity(){

        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(this.roleId)
                .userId(this.userId)
                .build();

        return UserRole.builder()
                .id(userRoleId)
                .build();
    }

}

package com.soundbrew.soundbrew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDTO {

    private UserDTO userDTO;

    private UserSubscriptionDTO userSubscriptionDTO;

    private UserRoleDTO userRoleDTO;

}

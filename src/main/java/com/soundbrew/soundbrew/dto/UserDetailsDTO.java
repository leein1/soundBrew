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

    private UserRoleDTO userRoleDTO;

    private UserSubscriptionDTO userSubscriptionDTO;



//    public UserDetailsDTO(UserDTO userDTO, UserRoleDTO userRoleDTO, UserSubscriptionDTO userSubscriptionDTO) {
//        this.userDTO = userDTO;
//        this.userRoleDTO = userRoleDTO;
//        this.userSubscriptionDTO = userSubscriptionDTO;
//    }

}

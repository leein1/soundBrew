package com.soundbrew.soundbrew.dto.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDetailsDTO implements UserDetails {

    private UserDTO userDTO;

    private UserRoleDTO userRoleDTO;

    private UserSubscriptionDTO userSubscriptionDTO;

    private List<GrantedAuthority> authorities;

    public UserDetailsDTO(String username, int userId, String nickname, List<GrantedAuthority> roles){

        // 생성자 내부에서 UserDTO를 직접 초기화
        this.userDTO = new UserDTO();
        this.userDTO.email = username;
        this.userDTO.userId = userId;
        this.userDTO.nickname = nickname;
        this.authorities = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    //  password는 null일수 없는 항목
    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    //  email은 null일수 없는 항목
    @Override
    public String getUsername() {
        return userDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return userDTO.isEmailVerified();
        return true;
    }


//    public UserDetailsDTO(UserDTO userDTO, UserRoleDTO userRoleDTO, UserSubscriptionDTO userSubscriptionDTO) {
//        this.userDTO = userDTO;
//        this.userRoleDTO = userRoleDTO;
//        this.userSubscriptionDTO = userSubscriptionDTO;
//    }

}

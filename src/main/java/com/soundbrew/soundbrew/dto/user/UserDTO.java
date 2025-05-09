package com.soundbrew.soundbrew.dto.user;

import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.BaseDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO{

    public int userId;


    public Integer subscriptionId;


    public String name;


    public String nickname;


    public String password;


    public String phoneNumber;


    public String email;


    public boolean emailVerified;


    private int creditBalance;


    public  String profileImagePath;


    public LocalDate birth;

    public boolean credentialsNonExpired;


    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .subscriptionId(this.subscriptionId)
                .name(this.name)
                .nickname(this.nickname)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .emailVerified(this.emailVerified)
                .creditBalance(this.creditBalance)
                .profileImagePath(this.profileImagePath)
                .birth(this.birth)
                .credentialsNonExpired(this.credentialsNonExpired)
                .build();
    }

}

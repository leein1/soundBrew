package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.domain.BaseEntity;
import com.soundbrew.soundbrew.domain.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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


    public String phonenumber;


    public String email;


    public boolean emailVerified;


    public  String profileImagePath;


    public LocalDate birth;


    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .subscriptionId(this.subscriptionId)
                .name(this.name)
                .nickname(this.nickname)
                .password(this.password)
                .phonenumber(this.phonenumber)
                .email(this.email)
                .emailVerified(this.emailVerified)
                .profileImagePath(this.profileImagePath)
                .birth(this.birth)
                .build();
    }

}

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


    public String phoneNumber;


    public String email;


    public boolean emailVerified;


    private int creditBalance;


    public  String profileImagePath;


    public LocalDate birth;

    // BaseDTO의 필드 포함
//    public UserDTO(int userId, Integer subscriptionId, String name, String nickname, String password,
//                   String phoneNumber, String email, boolean emailVerified, int creditBalance,
//                   String profileImagePath, LocalDate birth, LocalDateTime createDate, LocalDateTime modifyDate) {
//        super.setCreateDate(createDate);
//        super.setModifyDate(modifyDate);
//        this.userId = userId;
//        this.subscriptionId = subscriptionId;
//        this.name = name;
//        this.nickname = nickname;
//        this.password = password;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.emailVerified = emailVerified;
//        this.creditBalance = creditBalance;
//        this.profileImagePath = profileImagePath;
//        this.birth = birth;
//    }

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
                .build();
    }

}

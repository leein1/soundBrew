package com.soundbrew.soundbrew.domain;

import com.soundbrew.soundbrew.dto.UserDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false)
    private int subscriptionId;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String nickname;

//    닉네임 변경 기록 필요

    @Column(nullable = false)
    private String password;

//    비밀번호 변경 기록 필요

    @Column(length = 20, nullable = false)
    private String phonenumber;

//    번호 변경 기록 필요

    @Column(nullable = false)
    private String email;

//  이메일 인증 default false라 null해도 됨
    @Column(nullable = true)
    private boolean emailVerified;

    @Column(nullable = true)
    private  String profileImagePath;

    @Column(nullable = true)
    private LocalDate birth;

//    BaseEntity 클래스를 상속
//    LocalDateTime modify_date;
//    LocalDateTime create_date;

    public void update(String name, String nickname, String password, String phonenumber, String email) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public User toEntity(UserDTO userDTO){
        User user = User.builder()
                .userId(userDTO.getUserId())
                .subscriptionId(userDTO.getSubscriptionId())
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .password(userDTO.getPassword())
                .phonenumber(userDTO.getPhonenumber())
                .email(userDTO.getEmail())
                .emailVerified(userDTO.isEmailVerified())
                .profileImagePath(userDTO.getProfileImagePath())
                .birth(userDTO.getBirth())
                .build();

        return user;
    }
}

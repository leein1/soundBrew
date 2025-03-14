package com.soundbrew.soundbrew.domain.user;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;


    @Column(nullable = true)
    private Integer subscriptionId;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String nickname;

//    닉네임 변경 기록 필요

    @Column(nullable = false)
    private String password;

//    비밀번호 변경 기록 필요

    @Column(length = 20, nullable = false)
    private String phoneNumber;

//    번호 변경 기록 필요

    @Column(nullable = false)
    private String email;

//  이메일 인증 default false라 null해도 됨
    @Column(nullable = true)
    private boolean emailVerified;

    @Column(nullable = false)
    private int creditBalance;


    @Column(nullable = true)
    private  String profileImagePath;

    @Column(nullable = true)
    private LocalDate birth;

    //  default = false;
    @Column(nullable = true)
    private boolean credentialsNonExpired;

//    BaseEntity 클래스를 상속
//    LocalDateTime modify_date;
//    LocalDateTime create_date;


    public void update(String name, String nickname, String password, String phoneNumber, String email) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

}

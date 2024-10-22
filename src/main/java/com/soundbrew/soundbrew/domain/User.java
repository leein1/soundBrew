package com.soundbrew.soundbrew.domain;

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
    private int subscription_id;

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
    private boolean email_verified;

    @Column(nullable = true)
    private  String profile_image_path;

    @Column(nullable = true)
    private LocalDate birth;

//    BaseEntity 클래스를 상속
//    LocalDateTime modify_date;
//    LocalDateTime create_date;
}

package com.soundbrew.soundbrew.domain;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class UserRoleId implements Serializable {
//
//    복합키를 위한 클래스라 클래스명 끝에 Id 붙임
//    @Embeddable , @EqualsAndhashCode 구현해야함
    private int role_id;

    private int user_id;


}

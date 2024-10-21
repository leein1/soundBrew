package com.soundbrew.soundbrew.domain;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

//  hibernate의 식별자 변경 제한을 우회하려는 행위 비권장됨
//  아규먼트에서 userId는 그대로 사용, roleId만 바꿔줘야 함
//    public void updateRoleId(int newRoleId){
//
//        this.id = UserRoleId.builder()
//                .roleId(newRoleId)
//                .userId(this.id.getUserId())
//                .build();
//    }
}

package com.soundbrew.soundbrew.domain;

import lombok.*;

<<<<<<< HEAD
import javax.persistence.*;
=======
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
>>>>>>> feature/kyoung
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
<<<<<<< HEAD

    private int roleId;

    private int userId;

//    id 업데이트시 해당 클래스에서 변경하는것 보다 UserRole 클래스에서 변경하는것이 타당해보여 옮김
//    public void change(int roleId, int userId){
//
//        this.roleId = roleId;
//        this.userId = userId;
//
//    }
=======
    private int role_id;

    private int user_id;
>>>>>>> feature/kyoung


}

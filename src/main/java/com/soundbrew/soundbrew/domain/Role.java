package com.soundbrew.soundbrew.domain;

import lombok.*;

<<<<<<< HEAD
import javax.persistence.*;
=======
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
>>>>>>> feature/kyoung

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
<<<<<<< HEAD
@Entity
=======
>>>>>>> feature/kyoung
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private int roleId;

    @Column(nullable = false)
    private String roleType;

    public void change(String roleType){
        this.roleType = roleType;
    }
=======
    private int role_id;

    @Column(nullable = false)
    private String role_type;
>>>>>>> feature/kyoung
}

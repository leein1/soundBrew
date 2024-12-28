package com.soundbrew.soundbrew.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(nullable = false)
    private String roleType;

    public void change(String roleType){
        this.roleType = roleType;
    }
}

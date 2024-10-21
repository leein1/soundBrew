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
    private int role_id;

    @Column(nullable = false)
    private String role_type;

    public void change(String role_type){
        this.role_type = role_type;
    }
}

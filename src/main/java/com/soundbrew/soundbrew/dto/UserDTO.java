package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseEntity {

    public int userId;


    public int subscriptionId;


    public String name;


    public String nickname;


    public String password;


    public String phonenumber;


    public String email;


    public boolean emailVerified;


    public  String profileImagePath;


    public LocalDate birth;

}

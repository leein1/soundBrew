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
}

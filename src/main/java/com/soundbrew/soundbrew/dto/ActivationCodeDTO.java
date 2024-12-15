package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.domain.ActivationCode;
import com.soundbrew.soundbrew.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivationCodeDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String activationCode;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @Column(nullable = true)
    private LocalDateTime createDate;

    public ActivationCode toEntity(){

        ActivationCode activationCode = ActivationCode.builder()
                .id(this.id)
                .user(this.user)
                .activationCode(this.activationCode)
                .expirationTime(this.expirationTime)
                .createDate(this.createDate)
                .build();

        return activationCode;
    }
}

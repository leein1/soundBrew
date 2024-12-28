package com.soundbrew.soundbrew.domain;

import lombok.*;
import org.hibernate.annotations.ManyToAny;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(value = {AuditingEntityListener.class})
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String activationCode;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @CreatedDate
    @Column(name = "create_date",updatable = false)
    private LocalDateTime createDate;



}

package com.soundbrew.soundbrew.domain.payment;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MusicCartTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int musicCartTransactionId; // 기본키

    private int musicId;

    private int userId;

    private String title;

    private String filePath;

    private String nickname;

    private String soundType;

    private int credit;

    private String status;
}

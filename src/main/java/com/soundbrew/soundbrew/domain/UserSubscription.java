package com.soundbrew.soundbrew.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserSubscription extends BaseEntity{

    @Id
    private int userId;

    @Column(nullable = false)
    private int subscriptionId;

    @Column(nullable = true)
    private int creditBalance;

    @Column(nullable = true)
    private LocalDateTime firstBillingDate;

    @Column(nullable = true)
    private LocalDateTime nextBillingDate;

    @Column(nullable = true)
    private String paymentStatus;

//    BaseEntity 상속으로 생략
//    private LocalDateTime create_date;
//
//    private LocalDateTime modify_date;
}

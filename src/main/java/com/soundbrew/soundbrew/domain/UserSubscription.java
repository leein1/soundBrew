package com.soundbrew.soundbrew.domain;

import lombok.*;

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
    private int user_id;

    @Column(nullable = false)
    private int subscription_id;

    @Column(nullable = true)
    private int credit_balance;

    @Column(nullable = true)
    private LocalDateTime first_billing_date;

    @Column(nullable = true)
    private LocalDateTime next_billing_date;

    @Column(nullable = true)
    private String payment_status;

//    BaseEntity 상속으로 생략
//    private LocalDateTime create_date;
//
//    private LocalDateTime modify_date;
}

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
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscription_id;

    @Column(nullable = false)
    private String subscription_type;

    @Column(nullable = false)
    private int subscription_price;

    @Column(nullable = false)
    private int credit_per_month;

//    BaseEntity 상속으로 생략
//    private LocalDateTime create_date;
//
//    private LocalDateTime modify_date;
}

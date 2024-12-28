package com.soundbrew.soundbrew.domain;

import lombok.*;
<<<<<<< HEAD
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;
=======
>>>>>>> feature/kyoung

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
<<<<<<< HEAD
    private int subscriptionId;

    @Column(nullable = false)
    private String subscriptionName;

    @Column(nullable = false)
    private int subscriptionPrice;

    @Column(nullable = false)
    private int creditPerMonth;
=======
    private int subscription_id;

    @Column(nullable = false)
    private String subscription_type;

    @Column(nullable = false)
    private int subscription_price;

    @Column(nullable = false)
    private int credit_per_month;
>>>>>>> feature/kyoung

//    BaseEntity 상속으로 생략
//    private LocalDateTime create_date;
//
//    private LocalDateTime modify_date;
<<<<<<< HEAD

    public void updatePrice(int SubscriptionPrice){
        this.subscriptionPrice = SubscriptionPrice;
    }

    public void updateCreditPerMonth(int creditPerMonth){
        this.creditPerMonth = creditPerMonth;
    }
=======
>>>>>>> feature/kyoung
}

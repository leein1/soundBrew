package com.soundbrew.soundbrew.domain.user;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionId;

    @Column(nullable = false)
    private String subscriptionName;

    @Column(nullable = false)
    private int subscriptionPrice;

    @Column(nullable = false)
    private int creditPerMonth;

//    BaseEntity 상속으로 생략
//    private LocalDateTime create_date;
//
//    private LocalDateTime modify_date;

    public void updatePrice(int SubscriptionPrice){
        this.subscriptionPrice = SubscriptionPrice;
    }

    public void updateCreditPerMonth(int creditPerMonth){
        this.creditPerMonth = creditPerMonth;
    }
}

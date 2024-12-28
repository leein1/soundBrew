package com.soundbrew.soundbrew.dto.user;

import com.soundbrew.soundbrew.domain.user.Subscription;
import com.soundbrew.soundbrew.dto.BaseDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO extends BaseDTO {

    private int subscriptionId;

    private String subscriptionName;

    private int subscriptionPrice;

    private int creditPerMonth;

    public void updatePrice(int SubscriptionPrice){
        this.subscriptionPrice = SubscriptionPrice;
    }

    public void updateCreditPerMonth(int creditPerMonth){
        this.creditPerMonth = creditPerMonth;
    }

    public Subscription toEntity(){

        Subscription subscription = Subscription.builder()
                .subscriptionId(this.subscriptionId)
                .subscriptionName(this.subscriptionName)
                .subscriptionPrice(this.subscriptionPrice)
                .creditPerMonth(this.creditPerMonth)
                .build();

        return subscription;
    }
}

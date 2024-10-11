package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Subscription;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class SubscriptionRepositoryTests {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,3).forEach(i -> {

            String subscription_type;
            int subscription_price;
            int credit_per_month=0;

            switch(i) {
                case 1:
                    subscription_type = "basic";
                    subscription_price = 10000;
                    credit_per_month = 50;
                    break;
                case 2:
                    subscription_type = "premium";
                    subscription_price = 20000;
                    credit_per_month = 100;
                    break;
                case 3:
                    subscription_type = "vip";
                    subscription_price = 30000;
                    credit_per_month = 200;
                    break;
                default:
                    throw new IllegalArgumentException("----------------------Invalid subscription_id: " + i);
            }

            Subscription subscription = Subscription.builder()
                    .subscription_id(i)
                    .subscription_type(subscription_type)
                    .subscription_price(subscription_price)
                    .credit_per_month(credit_per_month)
                    .build();

            Subscription result = subscriptionRepository.save(subscription);

            log.info("----------------------subscription - info"
                    + subscription.getSubscription_id()
                    + " | " +
                    subscription.getSubscription_type());
        });
    }



}


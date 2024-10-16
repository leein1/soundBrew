package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.UserSubscription;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class UserSubscriptionRepositoryTests {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Test
    public void testInsert(){
        UserSubscription userSubscription = UserSubscription.builder()
                .user_id(2)
                .subscription_id(1)
                .build();
//        log.info("user_id: {}, subscription_id: {}", userSubscription.getUser_id(), userSubscription.getSubscription_id());
        UserSubscription result = userSubscriptionRepository.save(userSubscription);

        log.info("user_subscription info : "
                + userSubscription.getUser_id()
                + "|"
                + userSubscription.getSubscription_id()
        );

    }
}

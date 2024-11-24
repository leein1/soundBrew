package com.soundbrew.soundbrew.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class SubscriptionServiceTests {

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    public void updateTest(){

        subscriptionService.updateSubscription(23,4);

    }

}

package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Subscription;
<<<<<<< HEAD
import com.soundbrew.soundbrew.domain.SubscriptionType;
=======
>>>>>>> feature/kyoung
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< HEAD
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
=======

>>>>>>> feature/kyoung
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class SubscriptionRepositoryTests {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void testInsert(){
<<<<<<< HEAD
//        IntStream.rangeClosed(1,3).forEach(i -> {
//
//            String subscription_type;
//            int subscription_price;
//            int credit_per_month=0;
//
//            switch(i) {
//                case 1:
//                    subscription_type = "basic";
//                    subscription_price = 10000;
//                    credit_per_month = 50;
//                    break;
//                case 2:
//                    subscription_type = "premium";
//                    subscription_price = 20000;
//                    credit_per_month = 100;
//                    break;
//                case 3:
//                    subscription_type = "vip";
//                    subscription_price = 30000;
//                    credit_per_month = 200;
//                    break;
//                default:
//                    throw new IllegalArgumentException("----------------------Invalid subscription_id: " + i);
//            }
//
//            Subscription subscription = Subscription.builder()
//                    .subscription_id(i)
//                    .subscription_type(subscription_type)
//                    .subscription_price(subscription_price)
//                    .credit_per_month(credit_per_month)
//                    .build();
//
//            Subscription result = subscriptionRepository.save(subscription);
//
//            log.info("----------------------subscription - info"
//                    + subscription.getSubscription_id()
//                    + " | " +
//                    subscription.getSubscription_type());
//        });

//        구독 정보는 중요정보로 판단 enum으로도 테스트 해봄
        Arrays.stream(SubscriptionType.values()).forEach(subscriptionType -> {

            Subscription subscription = Subscription.builder()
                    .subscriptionName(subscriptionType.getSubscriptionName())
                    .subscriptionPrice(subscriptionType.getSubscriptionPrice())
                    .creditPerMonth(subscriptionType.getCreditPerMonth())
=======
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
>>>>>>> feature/kyoung
                    .build();

            Subscription result = subscriptionRepository.save(subscription);

<<<<<<< HEAD
            log.info("subscription save result : " + result.getSubscriptionId()
                        + " subscription_name : " + result.getSubscriptionName()
                        + " subscription_price : " + result.getSubscriptionPrice()
                        + " credit_per_month : " + result.getCreditPerMonth()
            );

        });
    }

//    무엇을 기준으로 찾을지 논의 필요 우선 구독제 이름으로 검색
//    나중에 Enum에 subscriptionId도 포함하는 것이 좋을것 같음
//    db에서는 auto_increment 를 기본값에서 빼는게 좋을 것 같음
    @Test
    public void testFindBySusbscriptionName(){

        String subscriptionName =  SubscriptionType.BASIC.getSubscriptionName();

        log.info(subscriptionRepository.findBySubscriptionName(subscriptionName));
    }

    @Test
    public void testFindBySubscriptionId(){

        int subscriptionId = 4;

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
    log.info("구독제 정보 : {}", subscription.toString() );
    }


    @Transactional
    @Rollback(false)
    @Test
    public void testUpdate(){

//        데이터베이스에서 검색할때는 subscriptionName 대입 수정해줘야 함
        String subscriptionName =  SubscriptionType.BASIC.getSubscriptionName();

        Optional<Subscription> result = subscriptionRepository.findBySubscriptionName(subscriptionName);

        Subscription subscription = result.orElseThrow();

        log.info("before update subscription : " + result);

        subscription.updatePrice(10000);
        subscription.updateCreditPerMonth(50);

        log.info("after update subscription : " + result);

    }


//    마찬가지 무엇을 기준으로 삭제를 할것인가
    @Test
    public void testDelete(){

//      데이터베이스에서 검색할때는 subscriptionName 대입 수정해줘야 함
        String subscriptionName =  SubscriptionType.BASIC.getSubscriptionName();

        Optional<Subscription> result = subscriptionRepository.findBySubscriptionName(subscriptionName);
        Subscription subscription = result.orElseThrow();

        int subscriptionId = subscription.getSubscriptionId();

        log.info("before delete subscriptionID : " + result);

        subscriptionRepository.deleteById(subscriptionId);

    }
=======
            log.info("----------------------subscription - info"
                    + subscription.getSubscription_id()
                    + " | " +
                    subscription.getSubscription_type());
        });
    }


>>>>>>> feature/kyoung

}


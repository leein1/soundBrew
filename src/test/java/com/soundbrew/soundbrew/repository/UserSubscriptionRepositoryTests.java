package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.user.UserSubscription;
import com.soundbrew.soundbrew.repository.user.UserSubscriptionRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

<<<<<<< HEAD
import java.util.NoSuchElementException;
import java.util.Optional;

=======
>>>>>>> feature/kyoung
@SpringBootTest
@Log4j2
public class UserSubscriptionRepositoryTests {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

<<<<<<< HEAD

//    실제 서비스에서는 회원이 구독을 했을때
//    user 테이블에서 subscription_id 컬럼을 수정 한 후
//    user_subscription 테이블에 insert해야 한다
    @Test
    public void testInsert(){
        UserSubscription userSubscription = UserSubscription.builder()
                .userId(16)
                .subscriptionId(4)
=======
    @Test
    public void testInsert(){
        UserSubscription userSubscription = UserSubscription.builder()
                .user_id(2)
                .subscription_id(1)
>>>>>>> feature/kyoung
                .build();
//        log.info("user_id: {}, subscription_id: {}", userSubscription.getUser_id(), userSubscription.getSubscription_id());
        UserSubscription result = userSubscriptionRepository.save(userSubscription);

        log.info("user_subscription info : "
<<<<<<< HEAD
                + userSubscription.getUserId()
                + "|"
                + userSubscription.getSubscriptionId()
        );

    }

//    회원 정보를 찾기 위해 입력해야할 필드 정해야 할것 같음.
    @Test
    public void testFindById(){
        int userId = 16;

        Optional<UserSubscription> result = userSubscriptionRepository.findById(userId);
        UserSubscription userSubscription = result.orElseThrow(
                () -> new NoSuchElementException("해당 userId(" + userId + ")에 대한 UserSubscription을 찾을 수 없습니다.")
        );
        log.info(userSubscription.toString());
    }


//    삭제 정책 필요!!! 어떤 필드를 입력받아 삭제할것인지 논의 필요
    @Test
    public void testDelete(){

    }

=======
                + userSubscription.getUser_id()
                + "|"
                + userSubscription.getSubscription_id()
        );

    }
>>>>>>> feature/kyoung
}

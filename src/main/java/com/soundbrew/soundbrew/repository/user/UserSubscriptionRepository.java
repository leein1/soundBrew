package com.soundbrew.soundbrew.repository.user;

import com.soundbrew.soundbrew.domain.user.UserSubscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {

    //대시보드 관련 기능 추가
    @Query("""
        SELECT u.userId, u.name, u.nickname, us.subscriptionId, us.modifyDate
        FROM UserSubscription us 
        JOIN User u ON us.userId = u.userId
        ORDER BY us.modifyDate DESC
    """)
    List<Object[]> findRecentSubscriptionChanges(Pageable pageable);

    // 5. 구독중인 회원들을 Subscription과 조인하여, 등급(예: basic, premium, vip)별 회원 수 집계,월간 구독 매출 계산
    @Query("""
        SELECT s.subscriptionName, COUNT(us) AS subscriberCount, COUNT(us) * s.subscriptionPrice AS monthlyRevenue
        FROM UserSubscription us, Subscription s
        WHERE us.subscriptionId = s.subscriptionId
        GROUP BY s.subscriptionName, s.subscriptionPrice
        """)
    List<Object[]> countUsersBySubscriptionType();
}

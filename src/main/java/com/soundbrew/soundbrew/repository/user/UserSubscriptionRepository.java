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
        SELECT us.modifyDate, u.userId, u.name, u.nickname, us.subscriptionId
        FROM UserSubscription us 
        JOIN User u ON us.userId = u.userId
        ORDER BY us.modifyDate DESC
    """)
    List<Object[]> findRecentSubscriptionChangesRaw(Pageable pageable);

    // 월간 구독 매출 계산
    @Query("""
        SELECT COALESCE(SUM(s.subscriptionPrice), 0)
        FROM UserSubscription us 
        JOIN Subscription s ON us.subscriptionId = s.subscriptionId
        WHERE us.modifyDate >= CURRENT_DATE - INTERVAL '1' MONTH
    """)
    Long calculateMonthlyRevenue();

    // 5. 구독중인 회원들을 Subscription과 조인하여, 등급(예: basic, premium, vip)별 회원 수 집계
    @Query("SELECT s.subscriptionName, COUNT(u) " +
            "FROM User u, Subscription s " +
            "WHERE u.subscriptionId IS NOT NULL AND u.subscriptionId = s.subscriptionId " +
            "GROUP BY s.subscriptionName")
    List<Object[]> countUsersBySubscriptionType();
}

package com.soundbrew.soundbrew.service.userSubscription;

import com.soundbrew.soundbrew.repository.user.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserSubscriptionStatisticImpl implements UserSubscriptionStatisticService{
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public Map<String, Object> getUserSubscriptionStatsForAdmin() {
        Map<String, Object> stats = new HashMap<>();

        // 최근 구독 변경 내역 (예: 최근 10건)
        List<Object[]> recentChanges = userSubscriptionRepository.findRecentSubscriptionChangesRaw(PageRequest.of(0, 10));
        stats.put("recentChanges", recentChanges);

        // 월간 구독 매출 계산
        Long monthlyRevenue = userSubscriptionRepository.calculateMonthlyRevenue();
        stats.put("monthlyRevenue", monthlyRevenue);

        // 구독중인 회원들을 Subscription과 조인하여, 등급별 회원 수 집계
        List<Object[]> subscriptionTypeCounts = userSubscriptionRepository.countUsersBySubscriptionType();
        stats.put("subscriptionTypeCounts", subscriptionTypeCounts);

        return stats;
    }
}

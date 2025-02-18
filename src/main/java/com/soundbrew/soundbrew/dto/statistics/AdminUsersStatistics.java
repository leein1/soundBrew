package com.soundbrew.soundbrew.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUsersStatistics {
    // 회원 통계
    int totalUsers;
    int newUsersToday;
    int newUsersWeek;
    int newUsersMonth;
    int subscribedUsers;
    int verifiedUsers;

    // 구독 관련 통계
    Map<String, Integer> basicUsers;
    Map<String, Integer> premiumUsers;
    Map<String, Integer> VIPUsers;
    int monthlyRevenue;
    Map<String, Object> recentSubscriptionChanges;
}

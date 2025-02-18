package com.soundbrew.soundbrew.service.user;

import com.soundbrew.soundbrew.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserStatisticServiceImpl implements UserStatisticService{
    private final UserRepository userRepository;

    @Override
    public Map<String, Object> getUsersStatsForAdmin() {
        Map<String, Object> stats = new HashMap<>();

        // 1. 총 회원 수
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);

        // 현재 시간 기준으로 신규 가입자 수를 계산하기 위한 날짜 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        // 2. 신규 가입자 수
        long newUsers1Day = userRepository.countNewUsers(oneDayAgo);
        long newUsers7Days = userRepository.countNewUsers(sevenDaysAgo);
        long newUsers30Days = userRepository.countNewUsers(thirtyDaysAgo);
        stats.put("newUsers1Day", newUsers1Day);
        stats.put("newUsers7Days", newUsers7Days);
        stats.put("newUsers30Days", newUsers30Days);

        // 3. 구독제 가입자 수 (subscriptionId가 NULL이 아닌 경우)
        long subscribedUsers = userRepository.countSubscribedUsers();
        stats.put("subscribedUsers", subscribedUsers);

        // 4. 이메일 인증된 활성 사용자 수
        long verifiedUsers = userRepository.countVerifiedUsers();
        stats.put("verifiedUsers", verifiedUsers);

        return stats;
    }
}

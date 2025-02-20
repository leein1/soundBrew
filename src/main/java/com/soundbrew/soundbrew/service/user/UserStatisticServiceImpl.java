package com.soundbrew.soundbrew.service.user;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.user.UserStatisticDTO;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserStatisticServiceImpl implements UserStatisticService{
    private final UserRepository userRepository;

    @Override
    public ResponseDTO<UserStatisticDTO> getUsersStatsForAdmin() {
        // 1. 총 회원 수
        int totalUsers = (int) userRepository.count();

        // 현재 시간 기준으로 신규 가입자 수 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        // 2. 신규 가입자 수 (최근 1일, 7일, 30일)
        int newUsersToday = (int) userRepository.countByCreateDateGreaterThanEqual(oneDayAgo);
        int newUsersWeek = (int) userRepository.countByCreateDateGreaterThanEqual(sevenDaysAgo);
        int newUsersMonth = (int) userRepository.countByCreateDateGreaterThanEqual(thirtyDaysAgo);

        // 3. 구독제 가입자 수
        int subscribedUsers = (int) userRepository.countBySubscriptionIdIsNotNull();

        // 4. 이메일 인증된 활성 사용자 수
        int verifiedUsers = (int) userRepository.countByEmailVerifiedTrue();

        // UserStatisticDTO 생성
        UserStatisticDTO userStatisticDTO = UserStatisticDTO.builder()
                .totalUsers(totalUsers)
                .newUsersToday(newUsersToday)
                .newUsersWeek(newUsersWeek)
                .newUsersMonth(newUsersMonth)
                .subscribedUsers(subscribedUsers)
                .verifiedUsers(verifiedUsers)
                .build();

        return ResponseDTO.<UserStatisticDTO>withSingleData()
                .dto(userStatisticDTO)
                .build();
    }
}

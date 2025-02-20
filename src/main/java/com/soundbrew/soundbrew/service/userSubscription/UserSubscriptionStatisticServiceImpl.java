package com.soundbrew.soundbrew.service.userSubscription;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.usersubscription.SubscriptionTotalStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.usersubscription.SubscriptionStatisticDTO;
import com.soundbrew.soundbrew.repository.user.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserSubscriptionStatisticServiceImpl implements UserSubscriptionStatisticService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public ResponseDTO<SubscriptionTotalStatisticDTO> getUserSubscriptionStatsForAdmin() {
        SubscriptionTotalStatisticDTO statisticDTO = new SubscriptionTotalStatisticDTO();

        // 최근 구독 변경 내역 (최근 5건)
        List<Object[]> recentChanges = userSubscriptionRepository.findRecentSubscriptionChanges(PageRequest.of(0, 5));
        List<SubscriptionStatisticDTO> recentSubscriptionChangeDTOs = recentChanges.stream()
                .map(obj -> SubscriptionStatisticDTO.builder()
                        .userId(((Number) obj[0]).longValue())   // userId
                        .name((String) obj[1])                 // userName
                        .nickname((String) obj[2])                 // userNickname
                        .subscriptionId(((Number) obj[3]).longValue())   // subscriptionId
                        .modifyDate((LocalDateTime) obj[4])           // modifyDate
                        .build())
                .toList();

        // 구독중인 회원들의 등급별 회원 수 집계, 월간 수입
        List<Object[]> typeCounts = userSubscriptionRepository.countUsersBySubscriptionType();
        List<SubscriptionStatisticDTO> subscriptionStatisticDTOS = typeCounts.stream()
                .map(obj -> SubscriptionStatisticDTO.builder()
                        .subscriptionName((String) obj[0])                // subscriptionName
                        .count(((Number) obj[1]).longValue())      // count
                        .monthlyRevenue(((Number) obj[2]).longValue())
                        . build())
                .toList();

        statisticDTO.setRecentSubscriptionChangeDTO(recentSubscriptionChangeDTOs);
        statisticDTO.setSubscriptionStatisticDTO(subscriptionStatisticDTOS);

        return ResponseDTO.<SubscriptionTotalStatisticDTO>withSingleData().dto(statisticDTO).build();
    }
}

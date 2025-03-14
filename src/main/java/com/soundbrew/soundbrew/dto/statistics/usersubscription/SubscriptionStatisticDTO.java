package com.soundbrew.soundbrew.dto.statistics.usersubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SubscriptionStatisticDTO {
    private String subscriptionName;
    private long count;
    private long monthlyRevenue;

    private Long userId;
    private String name;
    private String nickname;
    private Long subscriptionId;
    private LocalDateTime modifyDate;
}
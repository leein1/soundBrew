package com.soundbrew.soundbrew.dto.statistics.usersubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SubscriptionTotalStatisticDTO {
    private List<SubscriptionStatisticDTO> recentSubscriptionChangeDTO;
    private List<SubscriptionStatisticDTO> subscriptionStatisticDTO;
}

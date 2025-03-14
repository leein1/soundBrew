package com.soundbrew.soundbrew.service.userSubscription;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.usersubscription.SubscriptionTotalStatisticDTO;

public interface UserSubscriptionStatisticService {
    public ResponseDTO<SubscriptionTotalStatisticDTO> getUserSubscriptionStatsForAdmin();
}

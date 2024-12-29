package com.soundbrew.soundbrew.service.subscription;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.SubscriptionDTO;

public interface SubscriptionService {

    ResponseDTO<SubscriptionDTO> findAllSubscriptions();

    ResponseDTO<SubscriptionDTO> findOneSubscription(int id);

    //    구독제 등록(또는 업데이트)
    ResponseDTO<String> addUserSubscription(int userId, int subscriptionId);

    //  구독제 변경
    ResponseDTO<String> updateUserSubscription(int userId, int subscriptionId);

    //    구독제 취소
    ResponseDTO<String> deleteUserSubscription(int userId, int subscriptionId);

}

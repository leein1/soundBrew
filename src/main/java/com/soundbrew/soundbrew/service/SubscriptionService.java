package com.soundbrew.soundbrew.service;

public interface SubscriptionService {

    //    구독제 등록(또는 업데이트)
    void updateSubscription(int userId, int subscriptionId);

    //    구독제 취소
    void deleteSubscription(int userId, int subscriptionId);

}

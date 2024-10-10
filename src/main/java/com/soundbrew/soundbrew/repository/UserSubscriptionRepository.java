package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
}

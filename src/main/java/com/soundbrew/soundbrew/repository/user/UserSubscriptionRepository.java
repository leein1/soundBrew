package com.soundbrew.soundbrew.repository.user;

import com.soundbrew.soundbrew.domain.user.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
}

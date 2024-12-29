package com.soundbrew.soundbrew.repository.subscription;

import com.soundbrew.soundbrew.domain.user.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findBySubscriptionName(String name);


}

package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Subscription;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}

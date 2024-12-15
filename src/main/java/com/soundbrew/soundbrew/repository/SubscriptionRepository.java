package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.Subscription;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findBySubscriptionName(String name);

}

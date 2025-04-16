package com.soundbrew.soundbrew.repository.payment;

import com.soundbrew.soundbrew.domain.payment.SubscriptionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionTransactionRepository extends JpaRepository<SubscriptionTransaction, Integer> {
}

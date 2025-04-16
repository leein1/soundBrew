package com.soundbrew.soundbrew.repository.payment;

import com.soundbrew.soundbrew.domain.payment.SubscriptionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionTransactionRepository extends JpaRepository<SubscriptionTransaction, Integer> {
    List<SubscriptionTransaction> findByUserId(int userId);
}

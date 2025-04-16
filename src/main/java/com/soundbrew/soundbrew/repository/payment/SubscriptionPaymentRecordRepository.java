package com.soundbrew.soundbrew.repository.payment;

import com.soundbrew.soundbrew.domain.payment.SubscriptionPaymentRecord;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionPaymentRecordDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPaymentRecordRepository extends JpaRepository<SubscriptionPaymentRecord, Integer> {
    SubscriptionPaymentRecord findByUserIdAndStatus(int userId, String status);
    void deleteByUserIdAndStatus(int userId, String status);
}

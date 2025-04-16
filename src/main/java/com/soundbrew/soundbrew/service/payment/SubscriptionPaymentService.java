package com.soundbrew.soundbrew.service.payment;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionPaymentRecordDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionTransactionDTO;

public interface SubscriptionPaymentService {
    // 1. 결제 미리 저장 ( payment record )
    ResponseDTO<String> subscriptionPaymentSave(SubscriptionPaymentRecordDTO subscriptionPaymentRecordDTO);

    // 2. 결제 정보 들고오기(검증을 위한) ( payment record )
    ResponseDTO<SubscriptionPaymentRecordDTO> subscriptionPaymentVerification(int userId, String status);//status : ready

    // 3. 결제 완료시 완료 DB에 저장 ( payment transaction )
    ResponseDTO<String> subscriptionTransactionSave(SubscriptionTransactionDTO subscriptionTransactionDTO);

    // 4. 결제 완료시 Record에 status 업데이트 (payment record)
    ResponseDTO<String> subscriptionRecordUpdate(SubscriptionPaymentRecordDTO subscriptionPaymentRecordDTO);

    // 5. 임시 결제 기록( record) 딜리트
    ResponseDTO<String> subscriptionRecordDelete(int userId, String status);
}

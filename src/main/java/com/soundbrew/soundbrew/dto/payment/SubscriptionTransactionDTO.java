package com.soundbrew.soundbrew.dto.payment;

import com.soundbrew.soundbrew.domain.payment.SubscriptionPaymentRecord;
import com.soundbrew.soundbrew.domain.payment.SubscriptionTransaction;
import com.soundbrew.soundbrew.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionTransactionDTO extends BaseDTO {
    private int subscriptionTransactionId;
    private int userId;
    private String orderId;        // 주문 번호
    private String customerKey;    // 고객 식별키
    private BigDecimal amount;        // 실제 거래 금액
    private String orderName;      // 주문명
    private String paymentKey;        // 선택적 저장
    private int creditAmount;
    private int subscriptionId;
//    private LocalDateTime approvedAt;

    public SubscriptionTransaction toEntity(){
        return SubscriptionTransaction.builder()
                .subscriptionTransactionId(this.subscriptionTransactionId)
                .userId(this.userId)
                .amount(this.amount)
                .paymentKey(this.paymentKey)
                .customerKey(this.customerKey)
                .orderName(this.orderName)
                .orderId(this.orderId)
                .creditAmount(this.creditAmount)
                .subscriptionId(this.subscriptionId)
                .build();
    }
}

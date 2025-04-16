package com.soundbrew.soundbrew.dto.payment;

import com.soundbrew.soundbrew.domain.payment.SubscriptionPaymentRecord;
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
public class SubscriptionPaymentRecordDTO {
    private int subscriptionPaymentRecordId;
    private int userId;
    private String orderId;        // 주문 번호
    private BigDecimal amount;           // 결제 금액
    private String status;
    private String customerKey;    // 고객 식별키
    private String orderName;      // 주문명
    private int creditAmount;
    private int subscriptionId;

//    private String paymentKey;

    public SubscriptionPaymentRecord toEntity() {
        return SubscriptionPaymentRecord.builder()
                .subscriptionPaymentRecordId(this.subscriptionPaymentRecordId)
                .userId(this.userId)
                .orderId(this.orderId)
                .amount(this.amount)
                .status(this.status)
                .customerKey(this.customerKey)
                .orderName(this.orderName)
                .creditAmount(this.creditAmount)
                .subscriptionId(this.subscriptionId)
//                .paymentKey(this.paymentKey)
                .build();
    }


//    private Long userId;
//    private Long relatedSubscriptionId;
//
//    private Boolean discount;
//    private BigDecimal discountAmount;
//
//    private BigDecimal amount;        // 구독 원가
//    private BigDecimal totalAmount;   // 최종 거래 금액
//    private BigDecimal creditAmount;  // 지급 크레딧 (선택)
//
//    private String status;            // trading, DONE, refund 등
//    private String paymentKey;
//    private String orderId;
//    private LocalDateTime approvedAt;
}


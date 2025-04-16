package com.soundbrew.soundbrew.domain.payment;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class SubscriptionTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionTransactionId;

    private int userId;
    private String orderId;        // 주문 번호
    private String customerKey;    // 고객 식별키
    private BigDecimal amount;        // 실제 거래 금액
    private String orderName;      // 주문명
    private String paymentKey;        // 선택적 저장
    private int creditAmount;
    private int subscriptionId;
//    private LocalDateTime createDate;
}

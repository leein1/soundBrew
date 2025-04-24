package com.soundbrew.soundbrew.domain.payment;

import com.soundbrew.soundbrew.domain.BaseEntity;
import com.soundbrew.soundbrew.domain.sound.Album;
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
public class SubscriptionPaymentRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subscriptionPaymentRecordId;
    private int userId;
    private String orderId;        // 주문 번호
    private BigDecimal amount;           // 결제 금액
    private String status;
    private String customerKey;    // 고객 식별키
    private String orderName;      // 주문명
    private int creditAmount;
    private int subscriptionId;

}

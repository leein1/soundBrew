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
//    private String paymentKey;
//    private LocalDateTime CreateDate;

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

//    일반 결제의 경우:
//    READY: 결제 준비
//    IN_PROGRESS: 결제 진행 중
//    DONE: 결제 완료
//    ABORTED: 결제 중단
//    CANCELED: 결제 취소
//    PARTIAL_CANCELED: 결제 부분 취소
//    EXPIRED: 유효 시간 만료
//
//    가상계좌의 경우:
//    READY: 가상계좌 발급
//    WAITING_FOR_DEPOSIT: 입금 대기
//    DONE: 입금 완료
//    CANCELED: 결제 취소
//    PARTIAL_CANCELED: 결제 부분 취소
}

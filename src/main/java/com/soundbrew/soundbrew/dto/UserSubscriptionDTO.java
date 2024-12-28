package com.soundbrew.soundbrew.dto;

import com.soundbrew.soundbrew.domain.BaseEntity;
import com.soundbrew.soundbrew.domain.UserSubscription;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDTO extends BaseDTO {

    @Id
    private int userId;

    @Column(nullable = false)
    private int subscriptionId;



    @Column(nullable = true)
    private LocalDateTime firstBillingDate;

    @Column(nullable = true)
    private LocalDateTime nextBillingDate;

    @Column(nullable = true)
    private String paymentStatus;

//    BaseEntity 상속으로 생략
//    private LocalDateTime create_date;
//
//    private LocalDateTime modify_date;

    // QueryDSL용 생성자
    public UserSubscriptionDTO(int userId, int subscriptionId, LocalDateTime firstBillingDate,
                               LocalDateTime nextBillingDate, String paymentStatus,
                               LocalDateTime createDate, LocalDateTime modifyDate) {
        super.setCreateDate(createDate);
        super.setModifyDate(modifyDate);// BaseDTO의 필드 초기화
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.firstBillingDate = firstBillingDate;
        this.nextBillingDate = nextBillingDate;
        this.paymentStatus = paymentStatus;
    }

    public UserSubscription toEntity(){

        UserSubscription userSubscription = UserSubscription.builder()
                .userId(this.userId)
                .subscriptionId(this.subscriptionId)
                .firstBillingDate(this.firstBillingDate)
                .nextBillingDate(this.nextBillingDate)
                .paymentStatus(this.paymentStatus)
                .build();

        return userSubscription;
    }
}

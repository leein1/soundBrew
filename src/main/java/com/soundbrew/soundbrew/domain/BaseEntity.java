package com.soundbrew.soundbrew.domain;

import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

//    DB에서 create_date는 전부
//    default CURRENT_TIMESTAMP 적용 됨
    @CreatedDate
    @Column(name = "createDate",updatable = false)
    private LocalDateTime createDate;

//    DB에서  modify_date 는 전부
//    default CURRENT_TIMESTAMP 및
//    on update CURRENT_TIMESTAMP 적용 됨
    @LastModifiedDate
    @Column(name = "modifyDate")
    private LocalDateTime modifyDate;
}

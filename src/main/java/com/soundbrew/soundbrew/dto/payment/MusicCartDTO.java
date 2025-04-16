package com.soundbrew.soundbrew.dto.payment;

import com.soundbrew.soundbrew.domain.payment.MusicCartRecord;
import com.soundbrew.soundbrew.domain.payment.MusicCartTransaction;
import com.soundbrew.soundbrew.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicCartDTO extends BaseDTO {
    private int musicCartRecordId; // MusicCartRecord 테이블의 기본키
    private int musicId;
    private int userId;
    private String title;
    private String filePath;
    private String nickname;
    private String soundType;
    private int credit;
    private String status;

    public MusicCartRecord toRecordEntity() {
        return MusicCartRecord.builder()
                .musicCartRecordId(this.musicCartRecordId)
                .musicId(this.musicId)
                .userId(this.userId)
                .title(this.title)
                .filePath(this.filePath)
                .nickname(this.nickname)
                .soundType(this.soundType)
                .credit(this.credit)
                .status(this.status)
                .build();
    }

    public MusicCartTransaction toTransactionEntity() {
        return MusicCartTransaction.builder()
                .musicId(this.musicId)
                .userId(this.userId)
                .title(this.title)
                .filePath(this.filePath)
                .nickname(this.nickname)
                .soundType(this.soundType)
                .credit(this.credit)
                .status(this.status)
                .build();
    }
}


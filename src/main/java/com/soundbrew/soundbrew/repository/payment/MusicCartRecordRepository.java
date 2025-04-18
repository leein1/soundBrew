package com.soundbrew.soundbrew.repository.payment;

import com.soundbrew.soundbrew.domain.payment.MusicCartRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicCartRecordRepository  extends JpaRepository<MusicCartRecord, Integer> {
    List<MusicCartRecord> findByUserId(int userId);
    MusicCartRecord findByUserIdAndMusicId(int userId, int musicId);
    void deleteByUserIdAndMusicId(int userId, int musicId);
}

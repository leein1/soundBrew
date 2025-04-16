package com.soundbrew.soundbrew.repository.payment;

import com.soundbrew.soundbrew.domain.payment.MusicCartTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicCartTransactionRepository extends JpaRepository<MusicCartTransaction, Integer> {
    MusicCartTransaction findByUserIdAndMusicId(int userId, int musicId);
    List<MusicCartTransaction> findByUserId(int userId);
}

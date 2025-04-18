package com.soundbrew.soundbrew.service.payment;

import com.soundbrew.soundbrew.domain.payment.MusicCartTransaction;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.MusicCartDTO;

import java.io.IOException;
import java.util.List;

public interface SoundPaymentService {
    // 1. 장바구니 정보 들고오기
    ResponseDTO<MusicCartDTO> getSoundCart(int userId);

    // 2. 장바구니 추가
    ResponseDTO<String> addSoundCart(MusicCartDTO musicCartDTO);

    // 3. 장바구니에서 제거
    ResponseDTO<String> deleteSoundCart(int userId, int musicId);

    // 4-1. 카트 -> 결제 기록 생성(결제 확정)
    ResponseDTO<String> addSoundTransaction(int userId, List<Integer> musicIds) throws IOException;

    // 4-2. 바로 결제 기록 생성( 결제 확정 )
    ResponseDTO<String> addSoundTransactionDirect(MusicCartDTO musicCartDTO);

    // 5. 결제 기록 불러오기(음원 재 다운로드)
    ResponseDTO<String> checkSoundTransaction(int userId, int musicId) throws IOException;

    // 6. 결제 기록 불러오기(순수 기록 보기)
    ResponseDTO<MusicCartDTO> getSoundTransaction(int userId);

    // 7-1. 카트에 있는지 검증
    Boolean isInCart(int userId, int musicId);

    // 7-2. 결제 기록에 있는지 검증
    Boolean isInTransaction(int userId, List<Integer> musicIds);
}

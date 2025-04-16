package com.soundbrew.soundbrew.service.payment;

import com.soundbrew.soundbrew.domain.payment.MusicCartRecord;
import com.soundbrew.soundbrew.domain.payment.MusicCartTransaction;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.MusicCartDTO;
import com.soundbrew.soundbrew.repository.payment.MusicCartRecordRepository;
import com.soundbrew.soundbrew.repository.payment.MusicCartTransactionRepository;
import com.soundbrew.soundbrew.service.file.FileService;
import com.soundbrew.soundbrew.service.sound.SoundsService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoundPaymentServiceImpl implements SoundPaymentService{
    private final MusicCartRecordRepository musicCartRecordRepository;
    private final MusicCartTransactionRepository musicCartTransactionRepository;

    private final UserService userService;
    private final FileService fileService;
    private final SoundsService soundsService;

    private final ModelMapper modelMapper;

    @Override
    public ResponseDTO<MusicCartDTO> getSoundCart(int userId) {
        List<MusicCartRecord> response = musicCartRecordRepository.findByUserId(userId);

        List<MusicCartDTO> dtos = response.stream()
                .map(entity -> modelMapper.map(entity, MusicCartDTO.class))
                .toList();

        return ResponseDTO.<MusicCartDTO>builder().dtoList(dtos).build();
    }

    @Override
    public ResponseDTO<String> addSoundCart(MusicCartDTO musicCartDTO) {
        musicCartRecordRepository.save(musicCartDTO.toRecordEntity());

        return ResponseDTO.<String>withMessage().message("카트에 음원을 추가했습니다.").build();
    }

    @Override
    public ResponseDTO<String> deleteSoundCart(int userId, int musicId) {
        musicCartRecordRepository.deleteByUserIdAndMusicId(userId,musicId);

        return ResponseDTO.<String>withMessage().message("선택한 음원을 장바구니에서 삭제했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO<String> addSoundTransaction(int userId, List<Integer> musicIds) throws IOException {
        int totalCredit = 0;

        // 1. 총 크레딧 계산
        for (Integer musicId : musicIds) {
            MusicCartRecord record = musicCartRecordRepository.findByUserIdAndMusicId(userId, musicId);
            if (record != null) {
                totalCredit += record.getCredit();
            }
        }

        // 2. 크레딧 차감 시도
        ResponseDTO<String> creditResponse = userService.minusCredit(userId, totalCredit);
        if (!creditResponse.getMessage().contains("정상적으로")) {
            return creditResponse; // 잔액 부족 등
        }

        // 3. 처리 진행
        for (Integer musicId : musicIds) {
            MusicCartRecord record = musicCartRecordRepository.findByUserIdAndMusicId(userId, musicId);
            if (record != null) {
                MusicCartDTO dto = modelMapper.map(record, MusicCartDTO.class);
                // 트랜잭션(최종 결제 기록) 테이블 추가
                musicCartTransactionRepository.save(dto.toTransactionEntity());
                // 음원 다운로드 카운트 올려주기
                soundsService.addCountDownload(record.getMusicId());
                // 파일 다운로드
                fileService.downloadSoundFile(record.getFilePath());
                // 카드 기록 삭제
                musicCartRecordRepository.deleteById(record.getMusicCartRecordId());
            }
        }

        return ResponseDTO.<String>withMessage()
                .message("결제가 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public ResponseDTO<String> checkSoundTransaction(int userId, int musicId) throws IOException {
        MusicCartTransaction musicCartTransaction = musicCartTransactionRepository.findByUserIdAndMusicId(userId,musicId);
        fileService.downloadSoundFile(musicCartTransaction.getFilePath());

        return ResponseDTO.<String>withMessage().message("음원을 다시 다운로드 했습니다.").build();
    }

    @Override
    public ResponseDTO<MusicCartDTO> getSoundTransaction(int userId) {
        List<MusicCartTransaction> list = musicCartTransactionRepository.findByUserId(userId);
        List<MusicCartDTO> musicCartDTO = list.stream()
                .map(musicCartTransaction -> modelMapper.map(musicCartTransaction, MusicCartDTO.class))
                .toList();

        return ResponseDTO.<MusicCartDTO>builder().dtoList(musicCartDTO).build();
    }


}

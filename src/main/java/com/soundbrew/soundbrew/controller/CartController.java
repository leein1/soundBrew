package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.MusicCartDTO;
import com.soundbrew.soundbrew.service.payment.SoundPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final SoundPaymentService soundPaymentService;

    // 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDTO<MusicCartDTO>> getSoundCart(@PathVariable int userId){
        ResponseDTO<MusicCartDTO> response = soundPaymentService.getSoundCart(userId);
        return ResponseEntity.ok(response);
    }

    // 장바구니 추가
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO<String>> addSoundCart(@RequestBody MusicCartDTO musicCartDTO) {
        ResponseDTO<String> response = soundPaymentService.addSoundCart(musicCartDTO);
        return ResponseEntity.ok(response);
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/{userId}/{musicId}")
    public ResponseEntity<ResponseDTO<String>> deleteSoundCart(@PathVariable int userId, @PathVariable int musicId) {
        ResponseDTO<String> response = soundPaymentService.deleteSoundCart(userId, musicId);
        return ResponseEntity.ok(response);
    }

    // 장바구니 결제 (트랜잭션 생성, 다운로드 처리 포함)
    @PostMapping("/transactions/{userId}")
    public ResponseEntity<ResponseDTO<String>> addSoundTransaction(@PathVariable("userId") int userId, @RequestBody List<Integer> musicIds) throws IOException {
        ResponseDTO<String> response = soundPaymentService.addSoundTransaction(userId, musicIds);
        return ResponseEntity.ok(response);
    }

    // 결제 후 재다운로드
    @PostMapping("/transactions/{userId}/download")
    public ResponseEntity<ResponseDTO<String>> checkSoundTransaction(@PathVariable("userId") int userId, @RequestBody int musicId) throws IOException {
        ResponseDTO<String> response = soundPaymentService.checkSoundTransaction(userId, musicId);
        return ResponseEntity.ok(response);
    }

    // 결제 내역 조회
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<ResponseDTO<MusicCartDTO>> getSoundTransaction(@PathVariable int userId) {
        ResponseDTO<MusicCartDTO> response = soundPaymentService.getSoundTransaction(userId);
        return ResponseEntity.ok(response);
    }
}


package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionPaymentRecordDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionTransactionDTO;
import com.soundbrew.soundbrew.dto.user.SubscriptionDTO;
import com.soundbrew.soundbrew.service.payment.SubscriptionPaymentService;
import com.soundbrew.soundbrew.service.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final SubscriptionPaymentService subscriptionPaymentService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public ResponseEntity<ResponseDTO<SubscriptionDTO>> getPlans(){
        ResponseDTO<SubscriptionDTO> subscriptionDTO = subscriptionService.getAllSubscription();

        return ResponseEntity.ok().body(subscriptionDTO);
    }

    @PostMapping("/draft")
    public ResponseEntity<ResponseDTO<String>> subscriptionPaymentSave(@RequestBody SubscriptionPaymentRecordDTO subscriptionPaymentRecordDTO){
        ResponseDTO<String> responseDTO = subscriptionPaymentService.subscriptionPaymentSave(subscriptionPaymentRecordDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/verification/{userId}/{status}")
    public ResponseEntity<ResponseDTO<SubscriptionPaymentRecordDTO>> subscriptionPaymentVerification(@PathVariable("userId") int userId, @PathVariable("status") String status){
        ResponseDTO<SubscriptionPaymentRecordDTO> response = subscriptionPaymentService.subscriptionPaymentVerification(userId, status);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/transaction/{userId}")
    public ResponseEntity<ResponseDTO<SubscriptionTransactionDTO>> getSubscriptionTransaction(@PathVariable("userId")int userId){
        ResponseDTO<SubscriptionTransactionDTO> response = subscriptionPaymentService.getSubscriptionTransaction(userId);

        return ResponseEntity.ok().body(response);
    }

    // 최종 결제를 끝내고 transcation에 생성(찐 결제 기록)
    @PostMapping("/transaction")
    public ResponseEntity<ResponseDTO<String>> subscriptionTransactionSave(@RequestBody SubscriptionTransactionDTO subscriptionTransactionDTO){
        ResponseDTO<String> response = subscriptionPaymentService.subscriptionTransactionSave(subscriptionTransactionDTO);

        return ResponseEntity.ok().body(response);
    }

    // 최종 결제를 끝내고 record에 update(결제 상태 마무리)
    @PatchMapping("/verification")
    public ResponseEntity<ResponseDTO<String>> subscriptionRecordVerificationUpdate(@RequestBody SubscriptionPaymentRecordDTO subscriptionPaymentRecordDTO){
        ResponseDTO<String> response = subscriptionPaymentService.subscriptionRecordUpdate(subscriptionPaymentRecordDTO);

        return  ResponseEntity.ok().body(response);
    }

    // 임시 결제 기록 ( Record ) 지우기
    @DeleteMapping("/draft/{userId}/{status}")
    public ResponseEntity<ResponseDTO<String>> subscriptionRecordDelete(@PathVariable("userId")int userId, @PathVariable("status")String status ){
        ResponseDTO<String> response = subscriptionPaymentService.subscriptionRecordDelete(userId, status);

        return ResponseEntity.ok().body(response);
    }


}

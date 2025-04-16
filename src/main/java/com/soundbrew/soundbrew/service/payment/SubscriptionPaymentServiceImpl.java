package com.soundbrew.soundbrew.service.payment;

import com.soundbrew.soundbrew.domain.payment.SubscriptionPaymentRecord;
import com.soundbrew.soundbrew.domain.payment.SubscriptionTransaction;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionPaymentRecordDTO;
import com.soundbrew.soundbrew.dto.payment.SubscriptionTransactionDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.repository.payment.SubscriptionPaymentRecordRepository;
import com.soundbrew.soundbrew.repository.payment.SubscriptionTransactionRepository;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService{
    private final SubscriptionPaymentRecordRepository subscriptionPaymentRecordRepository;
    private final SubscriptionTransactionRepository subscriptionTransactionRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Override
    public ResponseDTO<String> subscriptionPaymentSave(SubscriptionPaymentRecordDTO subscriptionPaymentRecordDTO) {
        subscriptionPaymentRecordRepository.save(subscriptionPaymentRecordDTO.toEntity());

        return ResponseDTO.<String>withMessage()
                .message("정상적으로 등록했습니다.")
                .build();
    }

    @Override
    public ResponseDTO<SubscriptionPaymentRecordDTO> subscriptionPaymentVerification(int userId, String status) {
        SubscriptionPaymentRecord response =subscriptionPaymentRecordRepository.findByUserIdAndStatus(userId,status);

        if (response == null) {
            return ResponseDTO.<SubscriptionPaymentRecordDTO>withMessage().message("사전 구독 결제 기록이 없습니다.").build();
        }

        SubscriptionPaymentRecordDTO result = modelMapper.map(response, SubscriptionPaymentRecordDTO.class);

        return ResponseDTO.<SubscriptionPaymentRecordDTO>withSingleData().dto(result).build();
    }

    @Override
    @Transactional
    public ResponseDTO<String> subscriptionTransactionSave(SubscriptionTransactionDTO subscriptionTransactionDTO) {
        subscriptionTransactionRepository.save(subscriptionTransactionDTO.toEntity());
        userService.updateCreditBalance(subscriptionTransactionDTO.getUserId(), subscriptionTransactionDTO.getCreditAmount());

        UserDTO existingUserDTO = userService.getUser(subscriptionTransactionDTO.getUserId()).getDto();
        if(existingUserDTO.subscriptionId != 1){
            //이미 구독중인 구독제가 있는관계 인원이가 만들어둔 메서드 활용
            userService.updateUserSubscription(subscriptionTransactionDTO.getUserId(), subscriptionTransactionDTO.getSubscriptionId());
        }else{
            userService.addUserSubscription(subscriptionTransactionDTO.getUserId(), subscriptionTransactionDTO.getSubscriptionId());
        }

        return ResponseDTO.<String>withMessage().message("정상적으로 요청을 마무리했습니다.").build();
    }

    @Override
    public ResponseDTO<String> subscriptionRecordUpdate(SubscriptionPaymentRecordDTO subscriptionPaymentRecordDTO) {
        SubscriptionPaymentRecord response = subscriptionPaymentRecordRepository.findByUserIdAndStatus(subscriptionPaymentRecordDTO.getUserId(), subscriptionPaymentRecordDTO.getStatus());

        SubscriptionPaymentRecordDTO mapper = modelMapper.map(response, SubscriptionPaymentRecordDTO.class);
        mapper.setStatus("DONE");
        subscriptionPaymentRecordRepository.save(mapper.toEntity());

        return ResponseDTO.<String>withMessage().message("정상적으로 변경했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO<String> subscriptionRecordDelete(int userId, String status) {
        subscriptionPaymentRecordRepository.deleteByUserIdAndStatus(userId,status);

        return ResponseDTO.<String>withMessage().message("기존 결제 기록을 삭제했습니다.").build();
    }

    @Override
    public ResponseDTO<SubscriptionTransactionDTO> getSubscriptionTransaction(int userId) {
        List<SubscriptionTransaction> subscriptionTransaction = subscriptionTransactionRepository.findByUserId( userId);

        List<SubscriptionTransactionDTO> subscriptionTransactionDTOS = subscriptionTransaction.stream()
                .map(dto -> modelMapper.map(dto, SubscriptionTransactionDTO.class))
                .toList();

        return ResponseDTO.<SubscriptionTransactionDTO>builder().dtoList(subscriptionTransactionDTOS).build();
    }

}

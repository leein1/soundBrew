package com.soundbrew.soundbrew.service.subscription;


import com.soundbrew.soundbrew.domain.user.Subscription;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.domain.user.UserSubscription;
import com.soundbrew.soundbrew.dto.*;
import com.soundbrew.soundbrew.dto.user.SubscriptionDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserSubscriptionDTO;
import com.soundbrew.soundbrew.handler.BusinessException;
import com.soundbrew.soundbrew.repository.subscription.SubscriptionRepository;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.repository.user.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionServiceImpl implements SubscriptionService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;


    @Override
    public ResponseDTO<SubscriptionDTO> getAllSubscription() {

        List<SubscriptionDTO> subscriptionDTOs = subscriptionRepository.findAll().stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDTO.class))
                .collect(Collectors.toList());

        log.info("repository result : {}", subscriptionDTOs);



//        return ResponseDTO.<SubscriptionDTO>withAll()
//                .requestDTO(new RequestDTO())
//                .dtoList(subscriptionDTOs)
//                .total(subscriptionDTOs.size())
//                .build();

        return ResponseDTO.withAll(new RequestDTO(),subscriptionDTOs,subscriptionDTOs.size());
    }

    @Override
    public ResponseDTO<SubscriptionDTO> getSubscription(int subscriptionId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();

        SubscriptionDTO subscriptionDTO = modelMapper.map(subscription, SubscriptionDTO.class);

        return ResponseDTO.<SubscriptionDTO>withSingleData()
                .dto(subscriptionDTO)
                .build();
    }

    // userSubscription 이랑, user 에 subscriptionid 가 각각있기 때문에, 둘 다 고쳐줘야함
    @Override
    @Transactional
    public ResponseDTO<String> updateSubscriptionId(int userId, int subscriptionId) {
        // 바꿀려는 하는 회원이 있나?
        User user = userRepository.findById(userId).orElseThrow();
        // 바꿀려는 구독 등급이 있나?
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        // 바꿀려는 회원의 회원구독DB 정보가 있나?
        UserSubscription userSubscription = userSubscriptionRepository.findById(user.getUserId())
                .orElseThrow(() -> new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_FOUND));

        //okay 회원db의 subscriptionId 값부터 바꾸자
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setSubscriptionId(subscriptionId);
        userRepository.save(userDTO.toEntity());

        UserSubscriptionDTO userSubscriptionDTO = modelMapper.map(userSubscription, UserSubscriptionDTO.class);
        userSubscriptionDTO.setSubscriptionId(subscription.getSubscriptionId());
        userSubscriptionRepository.save(userSubscriptionDTO.toEntity());

        return ResponseDTO.<String>withMessage().message("회원의 구독 등급을 변경했습니다.").build();
    }

    @Override
    public ResponseDTO<String> updatePaymentStatus(int userId, boolean paymentStatus) {
//        if (!paymentStatus.equalsIgnoreCase("true") && !paymentStatus.equalsIgnoreCase("false")) {
//            throw new IllegalArgumentException("부적절한 결제 상태 표현 : " + paymentStatus);
//        }

        // 바꾸자 하는 회원이 있나?
        User user = userRepository.findById(userId).orElseThrow();

        // okay, 그러면 바꾸자 하는 회원의 회원구독DB 정보가 있나?
        UserSubscription userSubscription = userSubscriptionRepository.findById(user.getUserId())
                .orElseThrow(() -> new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_FOUND));
        // dto로 변환,
        UserSubscriptionDTO userSubscriptionDTO = modelMapper.map(userSubscription, UserSubscriptionDTO.class);

        userSubscriptionDTO.setPaymentStatus(paymentStatus);

        userSubscriptionRepository.save(userSubscriptionDTO.toEntity());

        return ResponseDTO.<String>withMessage().message("회원의 결제 상태를 관리자 권한으로 변경했습니다.").build();
    }

    @Override
    public ResponseDTO<String> updateSubscription(int subscriptionId,SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();

        SubscriptionDTO dto = modelMapper.map(subscription, SubscriptionDTO.class);

        dto.setSubscriptionPrice(subscriptionDTO.getSubscriptionPrice());
        dto.setCreditPerMonth(subscription.getCreditPerMonth());

        subscriptionRepository.save(dto.toEntity());

        return ResponseDTO.<String>withMessage().message("구독제의 정보를 관리자 권한으로 변경했습니다.").build();
    }


//    //    구독제 등록
////    id만 받는다고 가정하고 작성
//    @Override
//    public ResponseDTO<String> addUserSubscription(int userId, int subscriptionId) {
//
//        //    유저가 존재 하는지 검증
//        User user = userRepository.findById(userId).orElseThrow();
//
//        //    set을 위해 DTO로 변환
//        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);
//
//        //  구독한 적이 없어야 함
//        if(existingUserDTO.subscriptionId == 0 || existingUserDTO.subscriptionId == null){
//            //    구독제 id set()
//            existingUserDTO.setSubscriptionId(subscriptionId);
//
//        } else{
//            throw new NoSuchElementException("이미 구독중인 구독제가 있습니다.");
//
//        }
//
//
//        //    user 테이블에 subscriptionId - update
//        userRepository.save(existingUserDTO.toEntity());
//
//        //    userSubscription 테이블에 인서트 하기 위한 준비
//
//        //    subscriptionId 검증
//        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
//
//        //    UserSubscriptionDTO 준비
//        UserSubscriptionDTO userSUbscriptionDTO = UserSubscriptionDTO.builder()
//                .userId(userId)
//                .subscriptionId(subscriptionId)
//                .build();
//
//        //    Entity로 변경 후  user_subscription 테이블에 save()
//        userSubscriptionRepository.save(userSUbscriptionDTO.toEntity());
//
//        return ResponseDTO.<String>withMessage()
//                .message("성공적으로 구독 하였습니다.")
//                .build();
//
//    }
//
////    구독제 수정
////    구독제를 중간에 더 높은 플랜 또는 낮은 플랜으로 변경할 경우???
//
//    @Override
//    public ResponseDTO<String> updateUserSubscription(int userId, int subscriptionId) {
//
//        //    유저가 존재 하는지 검증
//        User user = userRepository.findById(userId).orElseThrow();
//
//        //    set을 위해 DTO로 변환
//        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);
//
//        ///  구독한 적이 있어야 함
//        if(existingUserDTO.subscriptionId == 0 || existingUserDTO.subscriptionId == null){
//            // 예외 throw
//            throw new NoSuchElementException("구독중인 구독제가 없습니다.");
//
//        } else{
//            // 변경한 구독제 subscriptionId set()
//            existingUserDTO.setSubscriptionId(subscriptionId);
//        }
//
//        //    user 테이블에 subscriptionId - update
//        userRepository.save(existingUserDTO.toEntity());
//
//
//        //    userSubscription 테이블에 인서트 하기 위한 준비
//
//        //    subscriptionId 검증
//        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
//
//        //    UserSubscriptionDTO 준비
//        /*
//        !!!!!!!!!!!!!!!!!!!!!!!!!!! 잔여 크레딧 계산식 필요
//         */
//        UserSubscriptionDTO userSUbscriptionDTO = UserSubscriptionDTO.builder()
//                .userId(userId)
//                .subscriptionId(subscriptionId)
//                .build();
//
//        //    Entity로 변경 후 save()
//        userSubscriptionRepository.save(userSUbscriptionDTO.toEntity());
//
//        return ResponseDTO.<String>withMessage()
//                .message("성공적으로 변경 하였습니다.")
//                .build();
//    }
//
//
////    구독제 삭제
////    id만 받는다고 가정하고 작성
//    @Override
//    public ResponseDTO<String> deleteUserSubscription(int userId, int subscriptionId) {
//
//        return null;
//    }


}

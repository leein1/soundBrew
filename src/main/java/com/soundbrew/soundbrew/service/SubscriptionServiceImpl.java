package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.Subscription;
import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.dto.UserSubscriptionDTO;
import com.soundbrew.soundbrew.repository.SubscriptionRepository;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionServiceImpl implements SubscriptionService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;


//    구독제 등록
//    id만 받는다고 가정하고 작성
    @Override
    public void updateSubscription(int userId, int subscriptionId) {

//        유저가 존재 하는지 검증
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

//        set을 위해 DTO로 변환
        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);

//        구독제 id set()
        existingUserDTO.setSubscriptionId(subscriptionId);

//        user 테이블에 subscriptionId - update
        userRepository.save(existingUserDTO.toEntity());

//        userSubscription 테이블에 인서트 하기 위한 준비

//        subscriptionId 검증
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() ->
                new NoSuchElementException(subscriptionId + " 번 구독제를 찾을 수 없습니다."));

//        UserSubscriptionDTO 준비
        UserSubscriptionDTO userSUbscriptionDTO = UserSubscriptionDTO.builder()
                .userId(userId)
                .subscriptionId(subscriptionId)
                .creditBalance(subscription.getCreditPerMonth())    // subscription의 크레딧값을 바로 잔여금액으로 넣어준다
                .build();

//        Entity로 변경 후 save()
        userSubscriptionRepository.save(userSUbscriptionDTO.toEntity());

    }


//    구독제 등록 및 변경
//    id만 받는다고 가정하고 작성
    @Override
    public void deleteSubscription(int userId, int subscriptionId) {

    }

//    구독제를 중간에 더 높은 플랜 또는 낮은 플랜으로 변경할 경우???
}

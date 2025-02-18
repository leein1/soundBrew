package com.soundbrew.soundbrew.Service;

import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.domain.user.UserSubscription;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Transactional
    @Test
    public void testRegister() {
        UserDTO userDTO = UserDTO.builder()
                .name("donghun")
                .nickname("donghun")
                .password("testTEST123!@#")
                .phoneNumber("01012341234")
                .email("ddjsjs12@naver.com")
                .build();


        log.info(userService.registerUser(userDTO).toString());
    }

    @Transactional
    @Test
    public void testGetAllUserWithDetails() {
        // userId 2를 가진 요청 객체 생성
        RequestDTO requestDTO = RequestDTO.builder()
                .keyword("ddjsjs12")
                .type("n")
                .build();

        // 전체 유저정보 + 구독제 + 역할을 조회
        ResponseDTO<UserDetailsDTO> response = userService.getAllUserWithDetails(requestDTO);

        // 결과를 로그에 출력
        log.info("User details for userId 2: {}", response);
    }

//    @Test
//    @DisplayName("대시보드 통계 정보 및 로그 출력 테스트")
//    public void testGetDashboardStats() {
//        // 서비스 메서드 호출
//        Map<String, Object> stats = userService.getDashboardStats();
//
//        // 회원 관련 통계 출력
//        System.out.println("=== 회원 통계 ===");
//        System.out.println("총 회원 수: " + stats.get("totalUsers"));
//        System.out.println("최근 1일 신규 가입자: " + stats.get("newUsersToday"));
//        System.out.println("최근 1주 신규 가입자: " + stats.get("newUsersThisWeek"));
//        System.out.println("최근 1개월 신규 가입자: " + stats.get("newUsersThisMonth"));
//        System.out.println("구독 가입자 수: " + stats.get("subscribedUsers"));
//        System.out.println("이메일 인증된 사용자 수: " + stats.get("verifiedUsers"));
//
//        // 구독 등급별 구독자 비율 출력
//        System.out.println("=== 구독 등급별 구독자 비율 ===");
//        @SuppressWarnings("unchecked")
//        Map<String, Long> subscriptionRatio = (Map<String, Long>) stats.get("subscriptionRatio");
//        subscriptionRatio.forEach((type, count) ->
//                System.out.println("구독 등급: " + type + " / 사용자 수: " + count)
//        );
//
//        // 월간 구독 매출 출력
//        System.out.println("=== 월간 구독 매출 ===");
//        System.out.println("월간 매출: " + stats.get("monthlyRevenue"));
//
//        // 최근 구독 변경 이력 출력
//        System.out.println("=== 최근 구독 변경 이력 ===");
//        @SuppressWarnings("unchecked")
//        List<UserSubscription> recentChanges = (List<UserSubscription>) stats.get("recentSubscriptionChanges");
//        recentChanges.forEach(us -> {
//            System.out.println("사용자ID: " + us.getUserId() +
//                    ", 구독ID: " + us.getSubscriptionId() +
//                    ", 최초 청구일: " + us.getFirstBillingDate() +
//                    ", 다음 청구일: " + us.getNextBillingDate() +
//                    ", 결제 상태: " + us.getPaymentStatus());
//        });
//    }
}

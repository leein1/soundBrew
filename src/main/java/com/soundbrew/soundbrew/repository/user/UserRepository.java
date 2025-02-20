package com.soundbrew.soundbrew.repository.user;

import com.soundbrew.soundbrew.domain.user.User;

import com.soundbrew.soundbrew.repository.user.search.UserSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer>, UserSearchRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByUserId(int userId);

    // 대시보드 관련기능 jpa Method를 생성.

    // 1. 총 회원 수
    long count();

    // 2. 신규 가입자 수 (최근 1일, 7일, 30일)
    long countByCreateDateGreaterThanEqual(LocalDateTime createDate);

    // 3. 구독제 가입자 수 (subscriptionId가 NULL이 아닌 경우)
    long countBySubscriptionIdIsNotNull();

    // 4. 이메일 인증된 활성 사용자 수
    long countByEmailVerifiedTrue();

}

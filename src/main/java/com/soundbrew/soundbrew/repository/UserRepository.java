package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u.userId FROM User u WHERE u.nickname = :nickname")
    Optional<Integer> findByNickname(@Param("nickname") String nickname);
}

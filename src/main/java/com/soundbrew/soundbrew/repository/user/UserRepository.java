package com.soundbrew.soundbrew.repository.user;

import com.soundbrew.soundbrew.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer>, UserSearchRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}

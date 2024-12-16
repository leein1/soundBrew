package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.repository.search.UserSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer>, UserSearchRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}

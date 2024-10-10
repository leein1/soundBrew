package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}

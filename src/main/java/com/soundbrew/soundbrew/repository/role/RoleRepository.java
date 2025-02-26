package com.soundbrew.soundbrew.repository.role;


import com.soundbrew.soundbrew.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findById(int id);
}

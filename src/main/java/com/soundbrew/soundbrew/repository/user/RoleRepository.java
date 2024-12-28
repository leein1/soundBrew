package com.soundbrew.soundbrew.repository.user;


import com.soundbrew.soundbrew.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}

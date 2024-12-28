package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.UserRole;
<<<<<<< HEAD
import com.soundbrew.soundbrew.domain.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

//    작동됨 쿼리문 혼합 불편
//    @Query("SELECT ur FROM UserRole ur WHERE ur.id.user_id = :user_id")
//    List<UserRole> findByUserId(@Param("user_id") int user_id);


//    jpa 필드 이름은 전부 카멜 케이스를 따라야 한다는 것을 처음 안 순간...
//
//    List<UserRole> findByIdUserId(@Param("user_id") int user_id);

    List<UserRole> findByIdUserId(int userId);

    List<UserRole> findByIdRoleId(int roleId);
=======
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
>>>>>>> feature/kyoung
}

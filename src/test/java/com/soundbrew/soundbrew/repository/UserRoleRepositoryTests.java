package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.UserRole;
import com.soundbrew.soundbrew.domain.UserRoleId;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class UserRoleRepositoryTests {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void testInsert(){
        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(4)
                .userId(11)
                .build();

        UserRole userRole = UserRole.builder()
                .id(userRoleId)
                .build();

        UserRole result = userRoleRepository.save(userRole);
        log.info("user_role"+result.getId());

    }

    @Test
    public void testSelect(){

        int userId = 11;
        int roleid = 4;

        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(roleid)
                .userId(userId)
                .build();

        log.info(userRoleRepository.findById(userRoleId));
    }

    @Test
    public void testFindByUserId(){

        int userId = 11;

        List<UserRole> result = userRoleRepository.findByIdUserId(userId);

        result.forEach(userRole -> log.info("user_role : " + userRole.getId()));
    }

    @Test
    public void testFindByRoleId(){

        int roleId = 4;

        List<UserRole> result = userRoleRepository.findByIdRoleId(roleId);

        result.forEach(userRole -> log.info("user_role : " + userRole.getId()));
    }

//    userId를 기준으로 roleId만 변경 할수 있도록
//    기존 키를 지우고 재 생성 - 타당한지 의논 필요
    @Transactional
    @Rollback(false)
    @Test
    public void testUpdate(){

//        기존 키 조회
        int userId = 11;
        int roleId = 4;

        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(roleId)
                .userId(userId)
                .build();

        Optional<UserRole> result = userRoleRepository.findById(userRoleId);

        UserRole userRole = result.orElseThrow();

//      기존 키 삭제
        userRoleRepository.delete(userRole);

        UserRoleId newUserRoleId = UserRoleId.builder()
                .roleId(5)
                .userId(11)
                .build();

        UserRole newUserRole = UserRole.builder()
                .id(newUserRoleId)
                .build();

        log.info(userRoleRepository.save(newUserRole));


    }

    @Test
    public void testDelete(){

        int userId = 11;
        int roleId = 5;

        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(roleId)
                .userId(userId)
                .build();

        UserRole userRole = UserRole.builder()
                .id(userRoleId)
                .build();

        userRoleRepository.delete(userRole);

        Optional<UserRole> result = userRoleRepository.findById(userRoleId);

        if(result.isEmpty()){
            log.info("userId : " + userId + " roleId : " + roleId + " has been deleted");
        } else {
            log.warn("userId : " + userId + " roleId : " + roleId + " still exists");
        }

    }
}

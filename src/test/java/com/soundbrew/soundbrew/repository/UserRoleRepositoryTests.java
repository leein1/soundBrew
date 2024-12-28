package com.soundbrew.soundbrew.repository;

<<<<<<< HEAD
import com.soundbrew.soundbrew.domain.user.UserRole;
import com.soundbrew.soundbrew.domain.user.UserRoleId;
import com.soundbrew.soundbrew.repository.user.UserRoleRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
=======
>>>>>>> feature/kyoung

@SpringBootTest
@Log4j2
public class UserRoleRepositoryTests {

    @Autowired
    private UserRoleRepository userRoleRepository;

<<<<<<< HEAD
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @BeforeEach
//    void testInsert2(){
////        유저 생성
//
//        User user = User.builder()
//                .user_id(17)
//                .subscription_id(4)
//                .name("user_" + 17)
//                .nickname("u_" + 17)
//                .password("password_" + 17)
//                .phonenumber("010-" + 17)
//                .email(17 + "_insert_test@test.com")
//                .build();
//
//        User userResult = userRepository.save(user);
//
//        log.info("user_id : "+userResult.getUser_id());
//
//
////      역할 생성
//        Role role = Role.builder()
//                .role_id(11)
//                .role_type("TEST")
//                .build();
//
//        Role roleResult = roleRepository.save(role);
//
//        log.info(roleResult);
//
////      복합키 생성 후 인서트
//        UserRoleId userRoleId = UserRoleId.builder()
//                .roleId(roleResult.getRole_id())
//                .userId(userResult.getUser_id())
//                .build();
//
//        UserRole userRole = UserRole.builder()
//                .id(userRoleId)
//                .build();
//
//        UserRole afterInsert = userRoleRepository.save(userRole);
//    }









    @Test
    void testInsert(){
        UserRoleId userRoleId = UserRoleId.builder()
                .roleId(4)
                .userId(16)
=======
    @Test
    void testInsert(){
        UserRoleId userRoleId = UserRoleId.builder()
                .role_id(3)
                .user_id(1)
>>>>>>> feature/kyoung
                .build();

        UserRole userRole = UserRole.builder()
                .id(userRoleId)
                .build();

        UserRole result = userRoleRepository.save(userRole);
        log.info("user_role"+result.getId());

    }
<<<<<<< HEAD

    @Test
    public void testSelect(){

        int userId = 16;
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
        int roleId = 4;

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
=======
>>>>>>> feature/kyoung
}

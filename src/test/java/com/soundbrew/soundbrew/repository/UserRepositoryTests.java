package com.soundbrew.soundbrew.repository;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.repository.search.UserSearchRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.awt.datatransfer.Clipboard;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;




    @Test
    public void testInsert(){

        IntStream.rangeClosed(100,100).forEach(i ->{
            User user = User.builder()
                    .subscriptionId(4)
                    .name("user_" + i)
                    .nickname("u_" + i)
                    .password("password_" + i)
                    .phoneNumber("010-" + i)
                    .email(i + "_insert_test@test.com")
                    .build();

            User result = userRepository.save(user);

            log.info("user_id : "+result.getUserId());
        });
    }

    @Test
    public void testSelect(){

        int user_id = 11;

        Optional<User> result = userRepository.findById(user_id);

        User user= result.orElseThrow();

        log.info(user);
    }

    @Test
    public void testSelectALl(){
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            log.info("회원 정보 : {}", user.toString());
            log.info("회원 생성일 : {}", user.getCreateDate());
            log.info("회원 수정일 : {}", user.getModifyDate());
        });
    }

    @Transactional
    @Test
    public void testUpdate(){

        int user_id = 11;

        Optional<User> result = userRepository.findById(user_id);

        User user = result.orElseThrow();

        user.update("dong"
                ,"2in1"
                ,"1234"
                , "010-7106-0745"
                ,"inwon.test@test.com");

//        User afterUpdate = userRepository.save(user);

//        log.info("user_name : "+afterUpdate.getName());
    }

    @Test
    public void testDelete(){

        int user_id = 11;

        userRepository.deleteById(user_id);

        Optional<User> result = userRepository.findById(user_id);

        if(result.isEmpty()){
            log.info("user_id : " + user_id + " has been deleted");
        } else {
            log.warn("user_id : " + user_id + " still exists");
        }
    }

    @Test
    public void testFindByEmail(){

        String email = "inwon.private@icloud.com";

        Optional<User> result = userRepository.findByEmail(email);

        if(result.isEmpty()){
            log.warn("찾을 수 없습니다.");
        }

        log.info(result.toString());
    }

    @Test
    public void testFindByNickname(){

        String nickname = "moon";
        Optional<User> result = userRepository.findByNickname(nickname);
        if(result.isEmpty()){
            log.info("찾을 수 없습니다.");
        }
        log.info(result.toString());
    }

    @Test
    public void testSearch(){
        ResponseDTO<UserDTO> responseDTO = userRepository.searchTest();

        List<UserDTO> testResult = responseDTO.getDtoList();

        log.info("갯수 : {}", testResult.size());
        testResult.forEach(dto ->log.info("결과 : {}", dto ));
    }

}

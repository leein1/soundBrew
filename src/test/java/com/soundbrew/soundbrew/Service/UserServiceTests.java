package com.soundbrew.soundbrew.Service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    void AllUserList(){

        RequestDTO requestDTO = RequestDTO.builder()
                .page(1)       // 첫 번째 페이지
                .size(10)      // 페이지 크기
                .keyword("") // 검색 키워드 설정
                .build();

        ResponseDTO responseDTO = userService.list(requestDTO);



        if (responseDTO == null) {
            log.warn("responseDTO is null");
        } else {
            log.info(responseDTO.toString());
        }
    }
}

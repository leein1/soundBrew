package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {


    @Override
    public void getAlluser() {

    }

    /*
        1. 주입 방식으로 사용할 것인지?
        2. 관리자의 필터링을 서비스에서 처리?
     */



//  아래는 예시 코드

    private final UserService userService;

    public ResponseDTO updateUser(UserDTO userDTO) {



        //        관리자가 접근할수 있는 영역 전처리
        userService.updateUser(userDTO);

        return null;
    }





}

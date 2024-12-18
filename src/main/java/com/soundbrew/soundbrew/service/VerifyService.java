package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.ResponseDTO;

public interface VerifyService {

    //    활성화 위한 메일 발송
    ResponseDTO<String> sendActivationCode(String email);

    //  회원 활성화 - 이메일 인증
    ResponseDTO<String> activateUser(String email,String activationCode);

    //  본인확인 - 로그인 된 상태더라도 정보 수정을 위해 접근할때 필요
    ResponseDTO<String> verifyPassword(String nickname, String password);
}

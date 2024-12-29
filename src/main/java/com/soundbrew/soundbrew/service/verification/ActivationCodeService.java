package com.soundbrew.soundbrew.service.verification;

import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.ActivationCodeDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;

import java.util.Map;

public interface ActivationCodeService {

    //코드 생성
    ActivationCodeDTO generateActivationCode();

    ResponseDTO<ActivationCodeDTO> getActivationCode(User user);

    //db insert
    ResponseDTO<String> addActivationCodeDTO(ActivationCodeDTO activationCodeDTO);

    //삭제
    ResponseDTO<String> deleteActivationCode(User user);

    //    활성화 위한 메일 발송
    ResponseDTO<String> sendActivationCode(String email);

    //  회원 활성화 - 이메일 인증
    ResponseDTO<String> activateUser(String email,String activationCode);

}

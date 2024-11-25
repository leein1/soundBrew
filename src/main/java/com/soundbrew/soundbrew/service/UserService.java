package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

//    전체 조회
//    Optional<List<UserDTO>> getAllUsers();

    ResponseDTO<UserDTO> getAllUsers();
//    한명 조회
//    UserDTO getUser(int userId);

//    한명 조회 테스트용
    Optional<UserDTO> getUser(int userId);

//    회원 가입
    void registerUser(UserDTO userDTO);

//    활성화 위한 메일 발송
    boolean sendActivationCode(String email);

//    회원 활성화 - 이메일 인증
    void activateUser(String email);

//    회원 정보 수정
    boolean updateUser(UserDTO userDTO);

//    본인확인 - 로그인 된 상태더라도 정보 수정을 위해 접근할때 필요
    boolean authentication(int userId, String password);

//    비밀번호 수정
    String updatePassword(int userId, String newPassword);

//    회원 삭제
    void deleteUser(int userId);


}

package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

//    전체 조회
//    Optional<List<UserDTO>> getAllUsers();

//    전체 조회
//    ResponseDTO<UserDTO> getAllUsers();

    public ResponseDTO<UserDTO> list(RequestDTO requestDTO);

//    한명 조회
//    UserDTO getUser(int userId);

//    한명 조회
    Optional<UserDTO> getUser(int userId);

//    회원 가입
    void registerUser(UserDTO userDTO);

//    활성화 위한 메일 발송
    boolean sendActivationCode(String email);

//    회원 활성화 - 이메일 인증
    boolean activateUser(String email,String activationCode);

//    회원 정보 수정
    void updateUser(UserDTO userDTO);

//    본인확인 - 로그인 된 상태더라도 정보 수정을 위해 접근할때 필요
    boolean authentication(int userId, String password);

//    비밀번호 수정
    String updatePassword(int userId, String newPassword);

//    회원 삭제
    void deleteUser(int userId);

//    프로필 이미지 업로드

    void saveProfileImage(int userId, MultipartFile file);

}

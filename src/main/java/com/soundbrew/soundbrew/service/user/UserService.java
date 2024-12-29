package com.soundbrew.soundbrew.service.user;


import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

//    전체 조회
//    Optional<List<UserDTO>> getAllUsers();

//    전체 조회
//    ResponseDTO<UserDTO> getAllUsers();

//    한명 조회
//    UserDTO getUser(int userId);



//    전체 조회
    public ResponseDTO<UserDTO> list(RequestDTO requestDTO);

//    한명 조회
    ResponseDTO<UserDTO> getUser(int userId);

    ResponseDTO<UserDTO> getUserByNickname(String nickname);
//    회원 가입
    void registerUser(UserDTO userDTO);

//    회원 정보 수정
    ResponseDTO updateUser(UserDTO userDTO);

//    비밀번호 수정
    String updatePassword(int userId, String newPassword);

//    회원 삭제
    void deleteUser(int userId);

    ResponseDTO<String> deleteUserByNickname(String nickname);
//    프로필 이미지 업로드

    void saveProfileImage(int userId, MultipartFile file);


    //  ---------------------------------------VerifyService 로 이동
//    활성화 위한 메일 발송
//    boolean sendActivationCode(String email);

    //  ---------------------------------------VerifyService 로 이동
//    회원 활성화 - 이메일 인증
//    boolean activateUser(String email,String providedActivationCode);

    //  ---------------------------------------VerifyService 로 이동
//    본인확인 - 로그인 된 상태더라도 정보 수정을 위해 접근할때 필요
//    ResponseDTO<String> verifyPassword(String nickname, String inputPassword);

}

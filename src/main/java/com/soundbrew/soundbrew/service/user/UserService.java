package com.soundbrew.soundbrew.service.user;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.dto.user.UserSubscriptionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

//    전체 조회
//    Optional<List<UserDTO>> getAllUsers();

//    전체 조회
//    ResponseDTO<UserDTO> getAllUsers();

//    한명 조회
//    UserDTO getUser(int userId);

//    유저 기능


    //  조회

    //  전체 조회 유저정보 + 구독제 + 역할
    ResponseDTO<UserDetailsDTO> getAllUserWithDetails(RequestDTO requestDTO);

    //  한명 조회(userId) - 유저정보 + 구독제 정보 + 역할
    ResponseDTO<UserDetailsDTO> getUserWithDetails(int userId);

    //  한명 조회(userId) - 유저 정보만
    ResponseDTO<UserDTO> getUser(int userId);

    Boolean isEmailExist(String email);

    ResponseDTO<UserDTO> getUserByEmail(String email);

    //  한명 조회(nickname) - 유저 정보만
    ResponseDTO<UserDTO> getUserByNickname(String nickname);

    Boolean isNicknameExist(String nickname);

    //  등록 및 수정

    //  회원 가입
    ResponseDTO<String> registerUser(UserDTO userDTO);

    //  회원 정보 수정
    ResponseDTO<String> updateUser(UserDTO userDTO);

    //  비밀번호 수정
    ResponseDTO<String> updatePassword(int userId, String newPassword);

    public ResponseDTO<String> verifyPassword(int userId, String inputPassword);

    //  프로필 이미지 관련

//    //  프로필 이미지 업로드
//    void saveProfileImage(int userId, MultipartFile file);
//    //  프로필 이미지 삭제
//    void deleteProfileImage(int userId);



    //  삭제

    //  회원 삭제
    ResponseDTO<String> deleteUser(int userId);

    //  닉네임으로 유저 삭제
    ResponseDTO<String> deleteUserByNickname(String nickname);




//    유저 + 구독 기능
    ResponseDTO<UserSubscriptionDTO> getUserSubscription(int userId);

    ResponseDTO<String> modifyUserSubscription(UserSubscriptionDTO userSubscriptionDTO);

    //  구독제 등록(또는 업데이트)
    ResponseDTO<String> addUserSubscription(int userId, int subscriptionId);

    //  구독제 변경
    ResponseDTO<String> updateUserSubscription(int userId, int subscriptionId);

    //  구독제 취소
    ResponseDTO<String> deleteUserSubscription(int userId);


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

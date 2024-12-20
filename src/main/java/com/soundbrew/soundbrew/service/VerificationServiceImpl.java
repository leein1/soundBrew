package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.ActivationCode;
import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.ActivationCodeDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.repository.ActivationCodeRepository;
import com.soundbrew.soundbrew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class VerificationServiceImpl implements VerificationService{

    private final UserRepository userRepository;
    private final ActivationCodeRepository activationCodeRepository;

    private final MailService mailService;

    private final ModelMapper modelMapper;


    //  중복되는 메서드 분리
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
    }

    private ResponseDTO<String> buildMessageResponse(String message) {
        return ResponseDTO.<String>withMessage().message(message).build();
    }


//    @Transactional
//    @Override
//    public ResponseDTO<String> sendActivationCode(String email) {
//        //        ddjsjs12@naver.com
////        moonody7731@naver.com
//
////        활성화 코드를 보내고 요청한 이메일과 코드를 묶어 기록 필요.
//
//        //  유저 검색 후 해당 userId의 기존 활성화 코드를 선 삭제
//        User user = userRepository.findByEmail(email).orElseThrow();
//        activationCodeRepository.deleteByUser(user);
//
//        //  활성화 코드 생성 - 6자리 코드 UUID UpperCase 코드 생성
//        String activationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//
//        //  코드 만료 시간 생성 - 생성시간으로 부터 10분
//        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
//
//        //  ActivationCodeDTO 빌드
//        ActivationCodeDTO activationCodeDTO = ActivationCodeDTO.builder()
//                .user(user)
//                .activationCode(activationCode)
//                .expirationTime(expirationTime)
//                .build();
//
//        //  엔티티 변환 후 save()
//        activationCodeRepository.save(activationCodeDTO.toEntity());
//
//        try {
//            mailService.send(email, "SoundBrew 계정 인증", "활성화 코드 입니다 : " + activationCode);
//
//            return ResponseDTO.<String>withMessage()
//                    .message("메일을 전송하였습니다.")
//                    .build();
//
//        }catch (Exception e){
//
//           return ResponseDTO.<String>withMessage()
//                   .message("메일 전송에 실패하였습니다.")
//                   .build();
//        }
//
//
//    }

    @Transactional
    @Override
    public ResponseDTO<String> sendActivationCode(String email) {

        // 조회 후 일치하는 데이터 일괄 삭제
        User user = this.findUserByEmail(email);
        activationCodeRepository.deleteByUser(user);

        //  활성화 코드 생성 - 6자리 코드 UUID UpperCase 코드 생성 + 유효시간 10 분
        String activationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        ActivationCodeDTO activationCodeDTO = ActivationCodeDTO.builder()
                .user(user)
                .activationCode(activationCode)
                .expirationTime(expirationTime)
                .build();

        activationCodeRepository.save(activationCodeDTO.toEntity());

        try {

            mailService.send(email, "SoundBrew 계정 인증", "활성화 코드 입니다 : " + activationCode);
            return this.buildMessageResponse("메일을 전송하였습니다.");

        } catch (Exception e) {

            log.error("메일 전송 실패", e);
            return this.buildMessageResponse("메일 전송에 실패하였습니다.");

        }

    }


//    @Transactional
//    @Override
//    public ResponseDTO<String> activateUser(String email, String providedActivationCode) {
//
//        //  사용자 검색
//        User user = userRepository.findByEmail(email).orElseThrow();
//
//        //  활성화 코드 조회
//        ActivationCode activationEntity = activationCodeRepository.findByUser(user).orElseThrow();
//
//        //  활성화 코드 유효성 검사( 보관한 기록 조회 후 대조)
//        if(!LocalDateTime.now().isBefore(activationEntity.getExpirationTime())){
//
//            return ResponseDTO.<String>withMessage()
//                    .message("유효 시간이 지났습니다.")
//                    .build();
//        } else if(!activationEntity.getActivationCode().equals(providedActivationCode)){
//
//            return ResponseDTO.<String>withMessage()
//                    .message("코드가 일치하지 않습니다.")
//                    .build();
//        }
//
//        try {
//            //  활성화
//            //  user 엔티티 to DTO
//            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
//            //  emailVerified 컬럼 true로 변경
//            userDTO.setEmailVerified(true);
//            //  save()
//            userRepository.save(userDTO.toEntity());
//
//            activationCodeRepository.delete(activationEntity);
//
//            return ResponseDTO.<String>withMessage()
//                    .message("확인 되었습니다.")
//                    .build();
//
//        } catch (Exception e){
//            throw new IllegalArgumentException();
//        }
//
//
//    }

    @Transactional
    @Override
    public ResponseDTO<String> activateUser(String email, String providedActivationCode) {

        User user = this.findUserByEmail(email);

        ActivationCode activationEntity = activationCodeRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("활성화 코드가 존재하지 않습니다."));

        if (LocalDateTime.now().isAfter(activationEntity.getExpirationTime())) {

            return this.buildMessageResponse("유효 시간이 지났습니다.");
        }

        if (!activationEntity.getActivationCode().equals(providedActivationCode)) {

            return this.buildMessageResponse("코드가 일치하지 않습니다.");
        }

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setEmailVerified(true);

        userRepository.save(user);
        activationCodeRepository.delete(activationEntity);

        return this.buildMessageResponse("확인 되었습니다.");
    }

//    @Override
//    public ResponseDTO<String> verifyPassword(String nickname, String inputPassword) {
////        유저가 존재 하는지 검증
////        User user = userRepository.findById(userId).orElseThrow(() ->
////                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));
//
////        Optional<User> result = userRepository.findById(userId);
//
//        User user = userRepository.findByNickname(nickname).orElseThrow();
//
//        String existingUserPassword = user.getPassword();
//
////            비밀 번호가 일치 하는 경우
//        if (!existingUserPassword.equals(inputPassword)) {
//
//            return ResponseDTO.<String>withMessage()
//                    .message("비밀번호가 일치하지 않습니다.")
//                    .build();
//        }
//
//        return ResponseDTO.<String>withMessage()
//                .message("확인되었습니다.")
//                .build();
//
//    }

    @Override
    public ResponseDTO<String> verifyPassword(String nickname, String inputPassword) {

        User user = this.findUserByEmail(nickname);

        if (!user.getPassword().equals(inputPassword)) {

            return this.buildMessageResponse("비밀번호가 일치하지 않습니다.");
        }

        return this.buildMessageResponse("확인되었습니다.");
    }
}

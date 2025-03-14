package com.soundbrew.soundbrew.service.verification;

import com.soundbrew.soundbrew.domain.ActivationCode;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.ActivationCodeDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.repository.ActivationCodeRepository;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.service.mail.MailService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ActivationCodeServiceImpl implements ActivationCodeService{

    private final ActivationCodeRepository activationCodeRepository;
    private final MailService mailService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Override
    public ActivationCodeDTO generateActivationCode() {

        //  활성화 코드 생성 - 6자리 코드 UUID UpperCase 코드 생성 + 유효시간 10 분
        String activationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        ActivationCodeDTO activationCodeDTO = ActivationCodeDTO.builder()
                .activationCode(activationCode)
                .expirationTime(expirationTime)
                .build();

        return activationCodeDTO;
    }

    @Override
    public ResponseDTO<ActivationCodeDTO> getActivationCode(User user) {

        ActivationCode activationCode = activationCodeRepository.findByUser(user).orElseThrow();
        ActivationCodeDTO activationCodeDTO = modelMapper.map(activationCode,ActivationCodeDTO.class);

        ResponseDTO<ActivationCodeDTO> responseDTO = ResponseDTO.<ActivationCodeDTO>withSingleData()
                .dto(activationCodeDTO)
                .build();

        return responseDTO;
    }

    @Override
    public ResponseDTO<String> deleteActivationCode(User user) {

        ActivationCode activationCode = activationCodeRepository.findByUser(user).orElseThrow();
        activationCodeRepository.delete(activationCode);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("활성화 코드 delete 성공")
                .build();

        return responseDTO;

    }

    @Transactional
    @Override
    public ResponseDTO<String> sendActivationCode(User user) {

        // activation_code 테이블 조회 후 유저 정보와 일치하는 데이터 일괄 삭제
//        Optional<ActivationCode> optionalActivationCode = activationCodeRepository.findByUser(user);
//
//        if(optionalActivationCode.isPresent()) {
//
//            this.deleteActivationCode(user);
//        }
        activationCodeRepository.findByUser(user)
                .ifPresent(activationCode -> this.deleteActivationCode(user));


        //  활성화 코드 생성 및 userid 지정
        ActivationCodeDTO activationCodeDTO = this.generateActivationCode();
        activationCodeDTO.setUser(user);

        // activation_code 테이블 save
       ActivationCode activationCode = activationCodeRepository.save(activationCodeDTO.toEntity());

        try {

            String email = user.getEmail();


            String text = "메일 인증 문자 입니다.\n발송 이후 10분간 유효합니다. \n http://ec2-3-39-121-136.ap-northeast-2.compute.amazonaws.com/activation \n" + activationCode.getActivationCode();
            mailService.send(email, "SoundBrew 계정 인증", text);

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message(email + "로 인증 메일을 전송하였습니다.")
                    .build();

            return responseDTO;

        } catch (Exception e) {

            log.error("메일 전송 실패", e);

            throw new RuntimeException("인증 메일 발송중 문제가 발생하였습니다.");


        }

    }

    public boolean validateActivationCode(String activationCode) {
        return true;
    }

    @Transactional
    @Override
    public ResponseDTO<String> activateUser(String providedActivationCode) {


        // 코드를 찾을 수 없는 경우 - 이메일 재 발송
        ActivationCode activationCode = activationCodeRepository.findByActivationCode(providedActivationCode)
                .orElseThrow(() -> new RuntimeException("해당 인증 코드를 찾을 수 없습니다."));

        // 활성화 코드는 있지만 해당 유저가 없는 경우 - 무결성 위반
        int userId = activationCode.getUser().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 이미 활성화 된 유저인 경우
        if(user.isEmailVerified()){
            throw new RuntimeException("이미 인증된 회원 입니다.");
        }

        // 코드의 유효시간이 지난 경우 - 이메일 재 발송
        if(activationCode.getExpirationTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효시간이 지났습니다. 인증 코드를 재발급 해주세요");
        }

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setEmailVerified(true);
        User afterEamilVerifiedUser = userDTO.toEntity();
        userRepository.save(afterEamilVerifiedUser);
        activationCodeRepository.delete(activationCode);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("인증 되었습니다.")
                .build();

        return responseDTO;
    }

}

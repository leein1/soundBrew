package com.soundbrew.soundbrew.service.verification;

import com.soundbrew.soundbrew.domain.ActivationCode;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.ActivationCodeDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.repository.ActivationCodeRepository;
import com.soundbrew.soundbrew.service.mail.MailService;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class ActivationCodeServiceImpl implements ActivationCodeService{

    private final ActivationCodeRepository activationCodeRepository;
    private final UserService userService;
    private final MailService mailService;
    private final ModelMapper modelMapper;


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
    public ResponseDTO<String> addActivationCodeDTO(ActivationCodeDTO activationCodeDTO) {

        ActivationCode activationCode = activationCodeRepository.save(activationCodeDTO.toEntity());

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("활성화 코드 insert 성공")
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

    @Override
    public ResponseDTO<String> sendActivationCode(String email) {

        // 조회 후 일치하는 데이터 일괄 삭제
        User user = userService.getUserByEmail(email).getDto().toEntity();
        this.deleteActivationCode(user);

//        //  활성화 코드 생성 - 6자리 코드 UUID UpperCase 코드 생성 + 유효시간 10 분
//        String activationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        ActivationCodeDTO activationCodeDTO = this.generateActivationCode();
        activationCodeDTO.setUser(user);

        this.addActivationCodeDTO(activationCodeDTO);

        try {

            mailService.send(email, "SoundBrew 계정 인증", "활성화 코드 입니다 : " + activationCodeDTO.getActivationCode());

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("메일을 전송하였습니다.")
                    .build();

            return responseDTO;

        } catch (Exception e) {

            log.error("메일 전송 실패", e);

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("메일 전송에 실패하였습니다.")
                    .build();

            return responseDTO;
        }

    }

    @Transactional
    @Override
    public ResponseDTO<String> activateUser(String email, String providedActivationCode) {

        User user = userService.getUserByEmail(email).getDto().toEntity();

        ActivationCode activationEntity = this.getActivationCode(user).getDto().toEntity();

        if (LocalDateTime.now().isAfter(activationEntity.getExpirationTime())) {

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("유효시간이 지났습니다.")
                    .build();

            return responseDTO;
        }

        if (!activationEntity.getActivationCode().equals(providedActivationCode)) {

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("코드가 일치하지 않습니다.")
                    .build();

            return responseDTO;
        }

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setEmailVerified(true);

        userService.updateUser(userDTO);
        this.deleteActivationCode(user);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("확인되었습니다.")
                .build();

        return responseDTO;
    }
}

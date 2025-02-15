package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.service.mail.MailService;
import com.soundbrew.soundbrew.service.user.UserService;
import com.soundbrew.soundbrew.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.soundbrew.soundbrew.util.PasswordGenerator.generatePassword;

@RestController
@RequestMapping("/api/help")
//@AllArgsConstructor
@RequiredArgsConstructor
@Log4j2
public class HelpContorller {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    @PostMapping("/password")
    public ResponseEntity<ResponseDTO<String>> resetPassword(@RequestBody UserDTO userDTO){

        log.info("---------------------------/api/help/resetPassword" + userDTO.toString());

        //  이메일과 이름이 일치하는 계정이 있는지 조회
        String email = userDTO.getEmail();
        String name = userDTO.getName();

        UserDTO result;

        try {
            log.info("---------------------------유저 조회 try");

            result = userService.getUserByEmailAndName(email, name).getDto();
        } catch (Exception e){

            ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                            .message("해당 계정을 찾을 수 없습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }

        //  있다면 임의의 비밀번호 생성
        String tempPassword = PasswordGenerator.generatePassword(8);

        //  암호화
        String encodedPassword = passwordEncoder.encode(tempPassword);

        //  임의의 비밀번호를 유저 데이터베이스 정보에 update + credentials_non_expired를 false로 변경
        result.setPassword(encodedPassword);
        result.setCredentialsNonExpired(false);

        userService.updateUser(result);

        //  이메일로 임의의 비밀번호 전송
        String text = "임시 비밀번호 입니다.\n" + tempPassword;
        mailService.send(email, "SoundBrew 임시 비밀번호 발급", text);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                .message("입력하신 메일로 임시 비밀번호를 발송했습니다.")
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

}

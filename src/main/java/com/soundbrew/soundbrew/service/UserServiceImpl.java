package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.ActivationCode;
import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.ActivationCodeDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.repository.ActivationCodeRepository;
import com.soundbrew.soundbrew.repository.SubscriptionRepository;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserValidator userValidator;
    private final ActivationCodeRepository activationCodeRepository;
    private final MailService mailService;

//    전체 조회
    @Override
    public ResponseDTO<UserDTO> getAllUsers() {

        List<UserDTO> userDTOs = userRepository.findAll().stream()
                .map(user -> modelMapper.map(user,UserDTO.class))
                .collect(Collectors.toList());

//        없으면 Opton.empty return
//        ResponseDTO 사용으로 수정

//        return userDTOs.isEmpty() ? Optional.empty() : Optional.of(userDTOs);

        Optional<List<UserDTO>> optionalDtoList = userDTOs.isEmpty()
                ? Optional.empty()
                : Optional.of(userDTOs);

        return ResponseDTO.<UserDTO>builder()
                .dtoList(optionalDtoList.orElse(Collections.emptyList())) // 값 설정
                .hasContent(optionalDtoList.isPresent())
                .build();
    }

//    한명 조회 - Optional 사용으로 주석
//    @Override
//    public UserDTO getUser(int userId) {
//
//        Optional<User> result = userRepository.findById(userId);
//
//        User user = result.orElseThrow(() ->
//                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다")
//        );
//
//        return modelMapper.map(user, UserDTO.class);
//    }

    @Override
    public Optional<UserDTO> getUser(int userId) {

        // Optional 객체로 조회
        Optional<User> result = userRepository.findById(userId);

//        없으면 Optional.empty() 반환
//        있으면 User를 UserDTO로 변환후 Optional<UserDTO>로 반환
        return result.map(user -> modelMapper.map(user,UserDTO.class));

    }

    //    회원 가입
    @Override
    public void registerUser(UserDTO userDTO) {

//        validation 적용 해야 할거 같은데 논의 필요.
//        UserDTO의 유효성 검사 측면
//        정책정의를 참고하여 UserDTO에 validation 적용 필요할듯

//        비밀번호 검증
//        String password = userDTO.getPassword();
//
//        if(!userValidator.isPasswordFormatValid(password)){
//            throw new IllegalArgumentException("비밀번호 규격에 맞지 않습니다.");
//        }
//
//        User user = userDTO.toEntity();

//        Optional 반환 필요 없을것으로 예상.
//        컨트롤러에서 예외 처리가 효율적으로 보임
//        return Optional.of(userRepository.save(user))
//                .map(User::getUserId)
//                .orElseThrow(() ->
//                        new RuntimeException("회원가입 실패"));

//       비밀번호 유효성 검사
        if(!userValidator.isPasswordFormatValid(userDTO.getPassword())){
//            return false;
            //  응답 객체 생성후 반환형 변경
            return;
        }

        userRepository.save(userDTO.toEntity());


    }

//    활성화 코드 보관함 필요 할것 같음 DB에서..? 자바에서...?
    @Override
    public boolean sendActivationCode(String email) {

//        ddjsjs12@naver.com
//        moonody7731@naver.com

//        활성화 코드를 보내고 요청한 이메일과 코드를 묶어 기록 필요.

        //  유저 검색 후 해당 userId의 기존 활성화 코드를 선 삭제
        Optional<User> result = userRepository.findByEmail(email);

        if(result.isEmpty()){
            return false;
        }

        User user = result.get();

        activationCodeRepository.deleteByUser(user);

        //  활성화 코드 생성
        String activationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        //  코드 만료 시간 생성
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        ActivationCodeDTO activationCodeDTO = ActivationCodeDTO.builder()
                .user(user)
                .activationCode(activationCode)
                .expirationTime(expirationTime)
                .build();

        //  코드와 유저 정보 기록
        activationCodeRepository.save(activationCodeDTO.toEntity());

        try {
            mailService.send(email, "SoundBrew 계정 인증", "활성화 코드 입니다 : " + activationCode);
        }catch (Exception e){
            log.error("메일 전송 실패: {}", email, e);
        }

        return true;
    }


//    유저가 재요청 했을때 논의 필요
    @Override
    public boolean activateUser(String email,String userActivationCode) {

        //  1. 사용자 검색
        Optional<User> result = userRepository.findByEmail(email);
        User user = result.get();

        //  2. 활성화 코드 조회
        Optional<ActivationCode> optionalActivationInfo = activationCodeRepository.findByUser(user);

        //  3. 활성화 코드 유효성 검사( 보관한 기록 조회 후 대조)
        ActivationCode activationInfo = optionalActivationInfo.get();

        //  4. 거부 조건
        if(!activationInfo.getActivationCode().equals(userActivationCode)){
            throw new IllegalArgumentException("유효하지 않은 코드 입니다.");
        }
        if(!LocalDateTime.now().isBefore(activationInfo.getExpirationTime())){
            throw new IllegalArgumentException("유효시간이 지났습니다. 다시 시도해주세요");
        }

        //  5. 활성화
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        userDTO.setEmailVerified(true);

        userRepository.save(userDTO.toEntity());

        activationCodeRepository.delete(activationInfo);

        return true;
    }


//    회원 정보 수정
//    비밀번호 변경은 따로 처리할것
//    프로필 이미지 변경 따로 처리할 것
    @Override
    public void updateUser(UserDTO userDTO) {

//        업데이트 방법 필요
//        이 메서드는 userDTO에 기존 정보도 전부 받아 온다고 가정 후 작성
        int userId = userDTO.getUserId();

        Optional<User> result = userRepository.findById(userId);

//         기존 사용자 정보 수정
        UserDTO existingUserDTO = modelMapper.map(result, UserDTO.class);

        existingUserDTO.setName(userDTO.getName());
        existingUserDTO.setNickname(userDTO.getNickname());
        existingUserDTO.setPhonenumber(userDTO.getPhonenumber());
        existingUserDTO.setEmail(userDTO.getEmail());
        existingUserDTO.setBirth(userDTO.getBirth());

        userRepository.save(existingUserDTO.toEntity());

    }


//    비밀번호 재 확인 - 본인 인증용
    @Override
    public boolean authentication(int userId, String password) {

//        유저가 존재 하는지 검증
//        User user = userRepository.findById(userId).orElseThrow(() ->
//                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

        Optional<User> result = userRepository.findById(userId);

        User user = result.get();

        String existingUserPassword = user.getPassword();

//            비밀 번호가 일치 하는 경우
        if (existingUserPassword.equals(password)) {

            return true;
        } else {

            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

    }

//    비밀번호만 수정
//    매개변수로 비밀번호만 받을지 DTO형태로 받을지 논의 필요
    @Override
    public String updatePassword(int userId, String newPassword) {

//        비밀번호가 비밀번호 양식에 맞는지 검증
        UserValidator userValidator = new UserValidator();

        if(!userValidator.isPasswordFormatValid(newPassword)) {

            return "비밀번호는 특수문자,대문자,숫자 각 1개 이상이 포함되어야 합니다.";
        }

//        유저가 존재 하는지 검증
//        User user = userRepository.findById(userId).orElseThrow(() ->
//                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

        Optional<UserDTO> optionalUserDTO = this.getUser(userId);

//        Opiton.empty()라면
        if(optionalUserDTO.isEmpty()){

            return "해당 유저가 존재하지 않습니다.";

        }else {

//        set을 위해 DTO로 변환
            UserDTO userDTO = optionalUserDTO.get();

//        비밀번호 set
            userDTO.setPassword(newPassword);

//        entity로 변환 후 save()
            userRepository.save(userDTO.toEntity());

            return "수정 되었습니다.";
        }



    }


//    회원 삭제
//    삭제시 구독 정보도 삭제 해야함
//    삭제시 역할도 삭제 해야함
    @Override
    public void deleteUser(int userId) {

//        유저가 존재 하는지 검증
        User user = userRepository.findById(userId).get();

        userRepository.delete(user);
    }




}

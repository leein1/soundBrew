package com.soundbrew.soundbrew.service;

import antlr.actions.python.CodeLexer;
import com.soundbrew.soundbrew.domain.Subscription;
import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.dto.UserSubscriptionDTO;
import com.soundbrew.soundbrew.repository.SubscriptionRepository;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.util.UserValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

//      예외 처리
        try{

            userRepository.save(userDTO.toEntity());

        }catch (DataIntegrityViolationException e){

//            throw new IllegalArgumentException("이미 사용중인 데이터 입니다.", e);
            return;
        }


    }

//    활성화 코드 보관함 필요 할것 같음
    @Override
    public boolean sendActivationCode(String email) {

//        활성화 코드를 보내고 요청한 이메일과 코드를 묶어 기록 필요.
        return false;
    }


//    유저가 재요청 했을때 논의 필요
    @Override
    public void activateUser(String email) {

//        1. 사용자 검색
//        2. 활성화 상태 조회
//        3. 활성화 코드 유효성 검사( 보관한 기록 조회 후 대조)
//        4. 사용자 상태 활성화
//        5. 보관했던 기록 처리
//
    }


//    회원 정보 수정
//    비밀번호 변경은 따로 처리할것
//    프로필 이미지 변경 따로 처리할 것
    @Override
    public boolean updateUser(UserDTO userDTO) {

//        업데이트 방법 필요
//        이 메서드는 userDTO에 기존 정보도 전부 받아 온다고 가정 후 작성
        int userId = userDTO.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

//         기존 사용자 정보 수정
        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);
        existingUserDTO.setName(userDTO.getName());
        existingUserDTO.setNickname(userDTO.getNickname());
        existingUserDTO.setPhonenumber(userDTO.getPhonenumber());
        existingUserDTO.setEmail(userDTO.getEmail());
        existingUserDTO.setBirth(userDTO.getBirth());

        try {
            userRepository.save(existingUserDTO.toEntity());
        }catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("이미 사용 중인 데이터입니다.", e);
        }

        return true;

    }


//    비밀번호 재 확인 - 본인 인증용
    @Override
    public boolean authentication(int userId, String password) {

//        유저가 존재 하는지 검증
//        User user = userRepository.findById(userId).orElseThrow(() ->
//                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

        Optional<User> result = userRepository.findById(userId);

//        유저를 찾을 수 없는 경우
        if(result.isEmpty() == true){

            return false;

        } else {

            User user = result.get();
            String existingUserPassword = user.getPassword();

//            비밀 번호가 일치 하는 경우
            if (existingUserPassword.equals(password)) {

                return true;

            } else {

                return false;
            }

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
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

        userRepository.delete(user);
    }




}

package com.soundbrew.soundbrew.service.user;


import com.soundbrew.soundbrew.domain.user.Subscription;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.domain.user.UserSubscription;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.*;
import com.soundbrew.soundbrew.repository.role.RoleRepository;
import com.soundbrew.soundbrew.repository.subscription.SubscriptionRepository;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.repository.user.UserRoleRepository;
import com.soundbrew.soundbrew.repository.user.UserSubscriptionRepository;
import com.soundbrew.soundbrew.service.subscription.SubscriptionService;
import com.soundbrew.soundbrew.service.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    @PersistenceContext
    private EntityManager entityManager;

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

//    private final ActivationCodeRepository activationCodeRepository;
//    private final MailService mailService;
//    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

////    전체 조회
//    @Override
//    public ResponseDTO<UserDTO> getAllUsers() {
//
//        List<UserDTO> userDTOs = userRepository.findAll().stream()
//                .map(user -> modelMapper.map(user,UserDTO.class))
//                .collect(Collectors.toList());
//
////        없으면 Opton.empty return
////        ResponseDTO 사용으로 수정
//
////        return userDTOs.isEmpty() ? Optional.empty() : Optional.of(userDTOs);
//
//        Optional<List<UserDTO>> optionalDtoList = userDTOs.isEmpty()
//                ? Optional.empty()
//                : Optional.of(userDTOs);
//
//
//        return ResponseDTO.<UserDTO>withAll()
//                .dtoList(optionalDtoList.orElse(Collections.emptyList())) // 값 설정
//                .build();
//    }

//    RequestDTO 받아 ResponseDTO 반환


    @Override
    public ResponseDTO<UserDetailsDTO> getAllUserWithDetails(RequestDTO requestDTO) {

        String[] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable pageable = requestDTO.getPageable("userId");

        log.info("UserService requestDTO : {}", requestDTO);
        log.info("UserService list() : " + pageable);

        //  Page<UserDetails> result = 유저레파지토리.서치인터페이스(types,keyword,pageable)
        Page<UserDetailsDTO> result = userRepository.findAllUserDetails(requestDTO).orElseThrow();

        if(result.isEmpty()){

            List<UserDetailsDTO> userDetailsDTOList = Collections.emptyList();

            return ResponseDTO.<UserDetailsDTO>withAll(requestDTO, userDetailsDTOList,0);
        }

        return ResponseDTO.withAll(requestDTO,result.getContent(),result.getSize());
    }

    @Override
    public ResponseDTO<UserDetailsDTO> getUserWithDetails(int id) {

        UserDetailsDTO userDetailsDTO = userRepository.findUserDetailsById(id).orElseThrow();

        ResponseDTO<UserDetailsDTO> responseDTO = ResponseDTO.<UserDetailsDTO>builder()
                .dto(userDetailsDTO)
                .build();

        return responseDTO;
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
    public ResponseDTO<UserDTO> getUser(int userId) {

        // Optional 객체로 조회
       User user = userRepository.findById(userId).orElseThrow();

        //  DTO변환
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return ResponseDTO.<UserDTO>withSingleData()
                .dto(userDTO)
                .build();

    }

    @Override
    public ResponseDTO<UserDTO> getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return ResponseDTO.<UserDTO>withSingleData()
                .dto(userDTO)
                .build();
    }

    @Override
    public ResponseDTO<UserDTO> getUserByNickname(String nickname) {

        User result = userRepository.findByNickname(nickname).orElseThrow();

        UserDTO userDTO = modelMapper.map(result,UserDTO.class);

        return ResponseDTO.<UserDTO>withSingleData()
                .dto(userDTO)
                .build();

    }

    //    회원 가입
    @Transactional
    @Override
    public ResponseDTO<String> registerUser(UserDTO userDTO) {

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

           ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("비밀번호가 적절하지 않습니다.")
                    .build();

            return responseDTO;
        }

        //  입력한 비밀번호 - 인코딩 전
        String beforeEncodePassword = userDTO.getPassword();
        //  비밀번호 인코딩
        userDTO.setPassword(passwordEncoder.encode(beforeEncodePassword));

        //  실제 save()
        User user = userRepository.save(userDTO.toEntity());
        log.info("user save() : {} ", user.toString());
        entityManager.flush();

        //  user_role 역할 테이블 save()
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .roleId(1)
                .userId(user.getUserId())
                .build();

        log.info("UserRoleDTO build : {}", userRoleDTO.toEntity().toString());

        userRoleRepository.save(userRoleDTO.toEntity());

        String nickname = user.getNickname();
        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message(nickname + "님 회원가입을 축하합니다!")
                .build();

        return responseDTO;

    }

    //  ---------------------------------------VerifyService 로 이동
//    활성화 코드 보관함 필요 할것 같음 DB에서..? 자바에서...?
//    @Override
//    public boolean sendActivationCode(String email) {
//
////        ddjsjs12@naver.com
////        moonody7731@naver.com
//
////        활성화 코드를 보내고 요청한 이메일과 코드를 묶어 기록 필요.
//
//        //  유저 검색 후 해당 userId의 기존 활성화 코드를 선 삭제
//        Optional<User> result = userRepository.findByEmail(email);
//
//        if(result.isEmpty()){
//            return false;
//        }
//
//        User user = result.get();
//
//        activationCodeRepository.deleteByUser(user);
//
//        //  활성화 코드 생성
//        String activationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//        //  코드 만료 시간 생성
//        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
//
//        ActivationCodeDTO activationCodeDTO = ActivationCodeDTO.builder()
//                .user(user)
//                .activationCode(activationCode)
//                .expirationTime(expirationTime)
//                .build();
//
//        //  코드와 유저 정보 기록
//        activationCodeRepository.save(activationCodeDTO.toEntity());
//
//        try {
//            mailService.send(email, "SoundBrew 계정 인증", "활성화 코드 입니다 : " + activationCode);
//        }catch (Exception e){
//            log.error("메일 전송 실패: {}", email, e);
//        }
//
//        return true;
//    }

//  ---------------------------------------VerifyService 로 이동
//    유저가 재요청 했을때 논의 필요
//    @Override
//    public boolean activateUser(String email,String providedActivationCode) {
//
//        //  1. 사용자 검색
//        Optional<User> result = userRepository.findByEmail(email);
//        User user = result.get();
//
//        //  2. 활성화 코드 조회
//        Optional<ActivationCode> optionalActivationInfo = activationCodeRepository.findByUser(user);
//
//        //  3. 활성화 코드 유효성 검사( 보관한 기록 조회 후 대조)
//        ActivationCode activationInfo = optionalActivationInfo.get();
//
//        //  4. 거부 조건
//        if(!activationInfo.getActivationCode().equals(providedActivationCode)){
//
//            logger.warn("Invalid activation code: userId={}, email={}, providedCode={}",
//                    user.getUserId(), user.getEmail(), providedActivationCode);
//            throw new IllegalArgumentException("유효하지 않은 코드 입니다.");
//        }
//        if(!LocalDateTime.now().isBefore(activationInfo.getExpirationTime())){
//
//            logger.warn("Expired activation code: userId={}, email={}, providedCode={}",
//                    user.getUserId(), user.getEmail(), providedActivationCode);
//            throw new IllegalArgumentException("유효시간이 지났습니다. 다시 시도해주세요");
//        }
//
//        //  5. 활성화
//        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
//        userDTO.setEmailVerified(true);
//
//        userRepository.save(userDTO.toEntity());
//        logger.info("Activated user: userId={}, name={}, email={}, providedCode={}, activationCode={}",
//                user.getUserId(), user.getName(), user.getEmail(), providedActivationCode, activationInfo.getActivationCode());
//
//        activationCodeRepository.delete(activationInfo);
//
//        return true;
//    }


    //    회원 정보 수정
    //    비밀번호 변경은 따로 처리할것
    //    프로필 이미지 변경 따로 처리할 것
    //    반환형 ResponseDTO로 변경
    @Override
    public ResponseDTO<String> updateUser(UserDTO userDTO) {

//        업데이트 방법 필요
//        이 메서드는 userDTO에 기존 정보도 전부 받아 온다고 가정 후 작성
//        int userId = userDTO.getUserId();
//
//        User result = userRepository.findById(userId).orElseThrow();

//        UserDTO existingUserDTO = this.getUser(userDTO.getUserId()).getDto();

//         기존 사용자 정보 수정
//        UserDTO existingUserDTO = modelMapper.map(result, UserDTO.class);

//        existingUserDTO.setName(userDTO.getName());
//        existingUserDTO.setNickname(userDTO.getNickname());
//        existingUserDTO.setPhoneNumber(userDTO.getPhoneNumber());
//        existingUserDTO.setEmail(userDTO.getEmail());
//        existingUserDTO.setBirth(userDTO.getBirth());


            userRepository.save(userDTO.toEntity());

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("수정 되었습니다.")
                    .build();

            return responseDTO;

    }

//  ---------------------------------------VerifyService 로 이동
//    비밀번호 재 확인 - 본인 인증용
//    userId -> nickname으로 변경
//    반환형 responseDTO로 변경
//    @Override
//    public ResponseDTO<String> verifyPassword(String nickname, String inputPassword) {
//
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

    //    비밀번호만 수정
    //    매개변수로 비밀번호만 받을지 DTO형태로 받을지 논의 필요
    @Override
    public ResponseDTO<String> updatePassword(int userId, String newPassword) {

        //        비밀번호가 비밀번호 양식에 맞는지 검증
        UserValidator userValidator = new UserValidator();

        if(!userValidator.isPasswordFormatValid(newPassword)) {

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("비밀번호는 6자리 이상 16자리 이하, 특수문자,대문자,숫자 각 1개 이상이 포함되어야 합니다.")
                    .build();

            return responseDTO;
        }

//        유저가 존재 하는지 검증
//        User user = userRepository.findById(userId).orElseThrow(() ->
//                new NoSuchElementException(userId + " 번 회원을 찾을 수 없습니다."));

        /*
        Optional<UserDTO> optionalUserDTO = this.getUser(userId);

//        Opiton.empty()라면
        if(optionalUserDTO.isEmpty()){

            return "해당 유저가 존재하지 않습니다.";

        }else {
        */

        //  유저 존재하는지 검증 및 DTO 변환
        UserDTO existingUserDTO = this.getUser(userId).getDto();

        //  비밀번호 set
        existingUserDTO.setPassword(newPassword);

        //  entity로 변환 후 save()
        //  userRepository.save(existingUserDTO.toEntity());
        this.updateUser(existingUserDTO);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("수정 되었습니다.")
                .build();

        return responseDTO;
    }

    @Override
    public ResponseDTO<String> verifyPassword(int userId, String inputPassword) {

        UserDTO userDTO = this.getUser(userId).getDto();

        if (!userDTO.getPassword().equals(inputPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("확인되었습니다.")
                .build();

        return responseDTO;
    }



    //    프로필 이미지 업로드
    @Override
    public void saveProfileImage(int userId, MultipartFile file) {

        //  유저 여부 확인
        User user = userRepository.findById(userId).orElseThrow();

        //  파일 확인
        if(file.isEmpty() || file.getContentType().equals("image/jpeg")){

        }

    }

    //  프로필 이미지 삭제
    @Override
    public void deleteProfileImage(int userId) {

    }

    //    회원 삭제

    //    삭제시 구독 정보도 삭제 해야함
    //    삭제시 역할도 삭제 해야함
    @Override
    public ResponseDTO<String> deleteUser(int userId) {

        User user = this.getUser(userId).getDto().toEntity();

        userRepository.delete(user);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("탈퇴 되었습니다.")
                .build();

        return responseDTO;
    }

    @Override
    public ResponseDTO<String> deleteUserByNickname(String nickname) {

        User user = this.getUserByNickname(nickname).getDto().toEntity();

        userRepository.delete(user);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("탈퇴 되었습니다.")
                .build();

        return responseDTO;
    }


    //  UserSubscription 관련

    // 한개 조회(userId)
    @Override
    public ResponseDTO<UserSubscriptionDTO> getUserSubscription(int userId){

       UserSubscription userSubscription = userSubscriptionRepository.findById(userId).orElseThrow();
       UserSubscriptionDTO userSubscriptionDTO = modelMapper.map(userSubscription,UserSubscriptionDTO.class);

       ResponseDTO<UserSubscriptionDTO> responseDTO = ResponseDTO.<UserSubscriptionDTO>withSingleData()
               .dto(userSubscriptionDTO)
               .build();

       return responseDTO;
    }

    // 수정
    @Override
    public ResponseDTO<String> modifyUserSubscription(UserSubscriptionDTO userSubscriptionDTO){

        userSubscriptionRepository.save(userSubscriptionDTO.toEntity());

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("수정 되었습니다.")
                .build();

        return responseDTO;
    }


    //  유저 구독제 등록
    //      id만 받는다고 가정하고 작성
    @Override
    public ResponseDTO<String> addUserSubscription(int userId, int subscriptionId) {

        /*
            유저가 존재 하는지 검증
        User user = userRepository.findById(userId).orElseThrow();

            set을 위해 DTO로 변환
        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);
*/

        //  검증
        UserDTO existingUserDTO = this.getUser(userId).getDto();
        SubscriptionDTO subscriptionDTO = subscriptionService.getSubscription(subscriptionId).getDto();

        //  구독한 적이 없어야 함
        if(existingUserDTO.subscriptionId != null){

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("이미 구독중인 구독제가 있습니다.")
                    .build();

            return responseDTO;
        }

        //    구독제 id set()
        existingUserDTO.setSubscriptionId(subscriptionId);

        //    user 테이블에 subscriptionId - update
        this.updateUser(existingUserDTO);

        //    UserSubscriptionDTO 준비
        UserSubscriptionDTO userSubscriptionDTO = UserSubscriptionDTO.builder()
                .userId(userId)
                .subscriptionId(subscriptionId)
                .build();

        //    Entity로 변경 후  user_subscription 테이블에 save()
        this.modifyUserSubscription(userSubscriptionDTO);

        String subscriptionName = subscriptionDTO.getSubscriptionName();

        return ResponseDTO.<String>withMessage()
                .message(subscriptionName + " 을 구독하였습니다.")
                .build();

    }

//    구독제 수정
//    구독제를 중간에 더 높은 플랜 또는 낮은 플랜으로 변경할 경우???

    @Override
    public ResponseDTO<String> updateUserSubscription(int userId, int subscriptionId) {

        //  검증
        UserDTO existingUserDTO = this.getUser(userId).getDto();
        SubscriptionDTO subscriptionDTO = subscriptionService.getSubscription(subscriptionId).getDto();


        ///  구독한 적이 있어야 함
        if(existingUserDTO.subscriptionId == null){

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("구독중인 구독제가 없습니다.")
                    .build();

            return responseDTO;

        }

        //    구독제 id set()
        existingUserDTO.setSubscriptionId(subscriptionId);

        //    user 테이블에 save()
        this.updateUser(existingUserDTO);


        //    userSubscription 테이블에 인서트 하기 위한 준비
        //    UserSubscriptionDTO 준비
        /*
        !!!!!!!!!!!!!!!!!!!!!!!!!!! 잔여 크레딧 계산식 필요
         */


        //  입력받은 유저 아이디로 UserSubscription 조회 후 DTO로 매핑
        UserSubscriptionDTO existingUserSubscriptionDTO = this.getUserSubscription(userId).getDto();

        //  변경할 구독제 subscriptionid set()
        existingUserSubscriptionDTO.setSubscriptionId(subscriptionId);

        //    Entity로 변경 후 save()
       this.modifyUserSubscription(existingUserSubscriptionDTO);

        String subscriptionName = subscriptionDTO.getSubscriptionName();

        return ResponseDTO.<String>withMessage()
                .message(subscriptionName + " 구독제로 변경 하였습니다.")
                .build();
    }


//    구독제 삭제
    //    id만 받는다고 가정하고 작성
    @Override
    public ResponseDTO<String> deleteUserSubscription(int userId) {

        //  검증
        UserDTO existingUserDTO = this.getUser(userId).getDto();
        UserSubscriptionDTO existingUserSubscriptionDTO = this.getUserSubscription(userId).getDto();

        //  User 테이블에서 해당 유저의 subscriptionId null로 업데이트
        existingUserDTO.setSubscriptionId(null);
        this.updateUser(existingUserDTO);

        //  UserSubscription 테이블에서 해당 유저의 레코드 삭제 - 크레딧 정보는 user쪽에 있으므로 삭제해도 됨
        userSubscriptionRepository.delete(existingUserSubscriptionDTO.toEntity());

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("구독을 취소했습니다.")
                .build();

        return responseDTO;
    }

}

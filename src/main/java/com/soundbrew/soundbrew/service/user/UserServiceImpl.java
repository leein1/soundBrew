package com.soundbrew.soundbrew.service.user;


import com.soundbrew.soundbrew.domain.user.Subscription;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.domain.user.UserSubscription;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.*;
import com.soundbrew.soundbrew.repository.ActivationCodeRepository;
import com.soundbrew.soundbrew.repository.role.RoleRepository;
import com.soundbrew.soundbrew.repository.subscription.SubscriptionRepository;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.repository.user.UserRoleRepository;
import com.soundbrew.soundbrew.repository.user.UserSubscriptionRepository;
import com.soundbrew.soundbrew.service.mail.MailService;
import com.soundbrew.soundbrew.service.subscription.SubscriptionService;
import com.soundbrew.soundbrew.service.util.UserValidator;
import com.soundbrew.soundbrew.service.verification.ActivationCodeService;
import com.soundbrew.soundbrew.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ActivationCodeService activationCodeService;
    private final MailService mailService;
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
//
//        String[] types = requestDTO.getTypes();
//        String keyword = requestDTO.getKeyword();
//        Pageable pageable = requestDTO.getPageable("userId");
//
//        log.info("UserService requestDTO : {}", requestDTO);
//        log.info("UserService list() : " + pageable); mn

        //  Page<UserDetails> result = 유저레파지토리.서치인터페이스(types,keyword,pageable)
        Page<UserDetailsDTO> result = userRepository.findAllUserDetails(requestDTO).orElseThrow();

        if(result.isEmpty()){

            List<UserDetailsDTO> userDetailsDTOList = Collections.emptyList();

            return ResponseDTO.<UserDetailsDTO>withAll(requestDTO, userDetailsDTOList,0);
        }

        return ResponseDTO.withAll(requestDTO,result.getContent(),(int)result.getTotalElements());
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
    public Boolean isEmailExist(String email) {

        return userRepository.findByEmail(email).isPresent();
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

    @Override
    public Boolean isNicknameExist(String nickname) {

        return userRepository.findByNickname(nickname).isPresent();
    }

//    @Override
//    public ResponseDTO<UserDTO> getUserByEmailAndName(String email, String name) {
//
//        User result = userRepository.findByEmailAndName(email,name).orElseThrow();
//
//        UserDTO userDTO = modelMapper.map(result,UserDTO.class);
//
//        return ResponseDTO.<UserDTO>withSingleData()
//                .dto(userDTO)
//                .build();
//    }

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

            throw new IllegalArgumentException("비밀번호가 적절하지 않습니다.");

//           ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
//                    .message("비밀번호가 적절하지 않습니다.")
//                    .build();
//
//            return responseDTO;
        }

        // user,user_role table save, email send 예외처리 이상함

        //  입력한 비밀번호 - 인코딩 전
        String beforeEncodePassword = userDTO.getPassword();
        //  비밀번호 인코딩
        userDTO.setPassword(passwordEncoder.encode(beforeEncodePassword));
        userDTO.setSubscriptionId(1);
        userDTO.setCredentialsNonExpired(true);
        userDTO.setProfileImagePath("default_profile_image.jpg");

        //  user 테이블 save()
        User user = userRepository.save(userDTO.toEntity());
        log.info("user save() : {} ", user.toString());
        entityManager.flush();

        //  user_role 역할 테이블 save()
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .roleId(1) // 회원
                .userId(user.getUserId())
                .build();

        log.info("UserRoleDTO build : {}", userRoleDTO.toEntity().toString());

        userRoleRepository.save(userRoleDTO.toEntity());

        //  메일 전송
        ResponseDTO<String> responseDTO = activationCodeService.sendActivationCode(user);

//            String nickname = user.getNickname();
//            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
//                    .message(nickname + "님 회원가입을 축하합니다!")
//                    .build();

        return responseDTO;



    }



    //    회원 정보 수정
    //    비밀번호 변경은 따로 처리할것
    //    프로필 이미지 변경 따로 처리할 것
    //    반환형 ResponseDTO로 변경
    @Override
    public ResponseDTO<String> updateUser(UserDTO userDTO) {

        User existingUser = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        UserDTO existingUserDTO = modelMapper.map(existingUser, UserDTO.class);

        Map<String,Object> changes = new HashMap<>();

        if( userDTO.getName() != null && !userDTO.getName().equals(existingUser.getName())){
            changes.put("name", userDTO.getName());
        }
        if (userDTO.getNickname() != null && !userDTO.getNickname().equals(existingUser.getNickname())) {
            changes.put("nickname", userDTO.getNickname());
        }
        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            changes.put("phoneNumber", userDTO.getPhoneNumber());
        }

        // 변경된 값이 없는 경우
        if (changes.isEmpty()){

            log.info("변경하려는 필드가 1개도 없음");

            return ResponseDTO.<String>withMessage()
                    .message("변경 사항이 없습니다.")
                    .build();
        }

        // 여러개를 한번에 수정하려고 한 경우
        if(changes.size() != 1){

            throw new IllegalArgumentException("한번에 하나만 수정 가능 합니다.");
        }

        changes.forEach((field,value) ->{
            log.info("userId : '{}' userEmail '{}' 수정 요청 : '{}' : '{}'", existingUserDTO.getUserId(), existingUserDTO.getEmail(),field, value);

            switch (field) {
                case "name":
                    existingUserDTO.setName((String) value);
                    break;
                case "nickname":
                    existingUserDTO.setNickname((String) value);
                    break;
                case "phoneNumber":
                    existingUserDTO.setPhoneNumber((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 필드입니다: " + field + "-" + value);
            }

        });

        userRepository.save(existingUserDTO.toEntity());

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("수정 되었습니다.")
                .build();

        return responseDTO;

    }

    @Override
    public ResponseDTO<String> generateTemporaryPassword(UserDTO userDTO) {

        //  이메일과 이름이 일치하는 계정이 있는지 조회
        String email = userDTO.getEmail();
        String name = userDTO.getName();

        User user = null;

        try{

            user = userRepository.findByEmail(email).orElseThrow();

        }catch (Exception e){

            ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                    .message("입력하신 메일을 찾을 수 없습니다.")
                    .build();
            return responseDTO;
        }

        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);

        // 이름이 일치하는지 검사
        if(!userDTO.name.equals(existingUserDTO.name)){

            ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                    .message("사용자 정보가 일치하지 않습니다.")
                    .build();
            return responseDTO;
        }

        //  있다면 임의의 비밀번호 생성
        String tempPassword = PasswordGenerator.generatePassword(8);

        //  암호화
        String encodedPassword = passwordEncoder.encode(tempPassword);

        //  임의의 비밀번호를 유저 데이터베이스 정보에 update + credentials_non_expired를 false로 변경
        existingUserDTO.setPassword(encodedPassword);
        existingUserDTO.setCredentialsNonExpired(false);

        userRepository.save(existingUserDTO.toEntity());

        //  이메일로 임의의 비밀번호 전송
        String text = "임시 비밀번호 입니다.\n" + tempPassword;
        mailService.send(email, "SoundBrew 임시 비밀번호 발급", text);

        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                .message("입력하신 메일로 임시 비밀번호를 발송했습니다.")
                .build();

        return responseDTO;
    }

    //    비밀번호만 수정
    //    매개변수로 비밀번호만 받을지 DTO형태로 받을지 논의 필요
    @Override
    public ResponseDTO<String> updatePassword(UserDTO userDTO) {

        //        비밀번호가 비밀번호 양식에 맞는지 검증
        UserValidator userValidator = new UserValidator();

        String newPassword = userDTO.getPassword();
        int userId = userDTO.getUserId();
        String email = userDTO.getEmail();

        if(!userValidator.isPasswordFormatValid(newPassword)) {

            ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                    .message("비밀번호는 6자리 이상 16자리 이하, 특수문자,대문자,숫자 각 1개 이상이 포함되어야 합니다. 다시 로그인해주세요.")
                    .build();

            return responseDTO;
        }

        User user = null;

        /** 사용자 존재 검증
         * 사용자가 비밀번호를 변경하는 2가지 경우
         * 비밀번호를 분실하여 임시 비밀번호를 발급받고 로그인 후 변경하는 경우 - 토큰에 username(email)만 포함되어 있음
         * 사용자가 정상적인 접근으로 스스로 비밀번호를 바꾸려 하는 경우 - 토큰에 userId 가 포함 되어 있음
         */
        if(userId != 0){

            user = userRepository.findByUserId(userId).orElseThrow();
        }else if(email != null){

            user = userRepository.findByEmail(email).orElseThrow();
        }

        UserDTO existingUserDTO = modelMapper.map(user, UserDTO.class);

        //  비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(newPassword);

        //  비밀번호 set
        existingUserDTO.setPassword(encodedPassword);
        existingUserDTO.setCredentialsNonExpired(true);

        //  entity로 변환 후 save()
        userRepository.save(existingUserDTO.toEntity());

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

    @Override
    public ResponseDTO<String> updateProfile(int userId, String fileName) {
        User user = userRepository.findByUserId(userId).orElseThrow();

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setProfileImagePath(fileName);

        userRepository.save(userDTO.toEntity());

        return ResponseDTO.<String>withMessage()
                .message("회원 정보에 프로필을 업데이트했습니다.")
                .build();
    }

    @Override
    public ResponseDTO<String> updateCreditBalance(int userId, int creditBalance) {
        UserDTO user = this.getUser(userId).getDto();
        user.setCreditBalance(creditBalance);
//        return this.updateUser(user);

        userRepository.save(user.toEntity());

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("수정 되었습니다.")
                .build();

        return responseDTO;
    }


//    //    프로필 이미지 업로드
//    @Override
//    public void saveProfileImage(int userId, MultipartFile file) {
//
//        //  유저 여부 확인
//        User user = userRepository.findById(userId).orElseThrow();
//
//        //  파일 확인
//        if(file.isEmpty() || file.getContentType().equals("image/jpeg")){
//
//        }
//
//    }
//
//    //  프로필 이미지 삭제
//    @Override
//    public void deleteProfileImage(int userId) {
//
//    }

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

        // 유저 존재하는지
        UserDTO existingUserDTO = this.getUser(userId).getDto();
        SubscriptionDTO subscriptionDTO = subscriptionService.getSubscription(subscriptionId).getDto();

        /*
            구독한 적이 없어야 함
            subscriptionId
            1 - free
            2 - basic
            3 - premium
            4 - vip
         */
        if(existingUserDTO.subscriptionId != 1){

            throw new RuntimeException("구독중인 구독제가 있습니다.");
        }

        //    구독제 id set()
        existingUserDTO.setSubscriptionId(subscriptionId);
        // credit set
        existingUserDTO.setCreditBalance(subscriptionDTO.getCreditPerMonth());

        //    user 테이블에 subscriptionId - update
       userRepository.save(existingUserDTO.toEntity());

        //    UserSubscriptionDTO 준비
        UserSubscriptionDTO userSubscriptionDTO = UserSubscriptionDTO.builder()
                .userId(userId)
                .subscriptionId(subscriptionId)
                .firstBillingDate(LocalDateTime.now())
                .nextBillingDate(LocalDateTime.now().plusMonths(1))
                .paymentStatus(true)
                .build();

        //    Entity로 변경 후  user_subscription 테이블에 save()
        this.modifyUserSubscription(userSubscriptionDTO);
//        userSubscriptionRepository.save(userSubscriptionDTO.toEntity());

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

        /*
            구독한 적이 없어야 함
            subscriptionId
            1 - free
            2 - basic
            3 - premium
            4 - vip
         */
        if(existingUserDTO.subscriptionId == 1){

            throw new RuntimeException("구독중인 구독제가 없습니다.");
        }

        /*
        구독제가 이전과 비교하여 더 비싼 금액인지, 저렴한 가격인지 처리 필요
         */

        //    구독제 id set()
        existingUserDTO.setSubscriptionId(subscriptionId);
        /*
        !!!!!!!!!!!!!!!!!!!!!!!!!!! 잔여 크레딧 계산식 필요
         */
        // credit set
        existingUserDTO.setCreditBalance(subscriptionDTO.getCreditPerMonth());

        //    user 테이블에 save()
        userRepository.save(existingUserDTO.toEntity());

        //  입력받은 유저 아이디로 UserSubscription 조회 후 DTO로 매핑
        UserSubscriptionDTO existingUserSubscriptionDTO = this.getUserSubscription(userId).getDto();

        //  변경할 구독제 subscriptionid set()
        existingUserSubscriptionDTO.setSubscriptionId(subscriptionId);
        existingUserSubscriptionDTO.setNextBillingDate(LocalDateTime.now().plusMonths(1));
        existingUserSubscriptionDTO.setPaymentStatus(true);

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

        //  User 테이블에서 해당 유저의 subscriptionId 1로 업데이트
        existingUserDTO.setSubscriptionId(4);
        userRepository.save(existingUserDTO.toEntity());

        //  UserSubscription 테이블에서 해당 유저의 레코드 삭제 - 크레딧 정보는 user쪽에 있으므로 삭제해도 됨
        userSubscriptionRepository.delete(existingUserSubscriptionDTO.toEntity());

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message("구독을 취소했습니다.")
                .build();

        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO<String> minusCredit(int userId, int credit) {
        UserDTO user = this.getUser(userId).getDto();
        int currentBalance = Optional.ofNullable(user.getCreditBalance()).orElse(0);

        if (currentBalance < credit) {
            return ResponseDTO.<String>withMessage()
                    .message("잔여 크레딧이 부족합니다.")
                    .build();
        }

        user.setCreditBalance(currentBalance - credit);
        userRepository.save(user.toEntity());

        return ResponseDTO.<String>withMessage()
                .message("크레딧이 정상적으로 차감되었습니다.")
                .build();
    }



}

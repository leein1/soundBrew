package com.soundbrew.soundbrew.security;

import com.soundbrew.soundbrew.dto.user.MemberSecurityDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*
        abstract method of UserDetails interface

        getAuthorities() - 사용자가 가진 모든 인가 정보 반환해야 함
        getPassword()
        getUsername()
        isAccountNonExpired()
        isAccountNonLocked()
        isCredentialsNonExpired()
        isEnabled()

        개발 단계
        UserDetails interface에 맞는 객체가필요
        내가 구현한 CustomUserDetailsService에서 반환
        PasswordEncoder(BCryptPasswordEncoder) - 시큐리티가 기본적으로 패스워드 엔코더를필요로 함 없는 경우 예외 발생
        권한 체크 - 예) 내 정보 -> 로그인을 해야만 한다
            시큐리티 config 클래스에 어노테이션(@EnableGlobalMethodSecurity(prePostEnable = true ) 적용 -> @PreAuthorize or @PostAuthorize 사전/사후 권한 체크 가능
            현재는 myInfo로 접근할때 사전에 체크할수 있도록 VewController에 시범 적용


        스프링 시큐리티 API에서 UserDetails 를 구현한 User 클래스(builder 지원)를 제공
         */

        log.info("이메일 기반 정보 조회(username - 유저 엔티티의 email),{}", username);

        //  우리가 작성한 User 클래스 아님 - 테스트용
        //  스프링 시큐리티 API에서 UserDetails 를 구현한 User 클래스(builder 지원)를 제공
//        UserDetails userDetails = User.builder()
//                .username("user1")
//                .password(passwordEncoder.encode("111"))
//                .roles("USER") // "ROLE_" 접두어를 자동으로 추가
//                .build();

        //  email로 실제 유저 조회
        UserDetailsDTO userDetailsDTO = userRepository.findUserDetailsByUsername(username).orElseThrow();

        // 스프링 시큐리티의 유저 객체의 authorities 필드를 위한 List
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if(userDetailsDTO.getUserRoleDTO().getRoleId() == 5){
            String role = "USER";
            log.info("로그인 시도 유저의 role 확인 : {}", role);
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        userDetailsDTO.getUserDTO().email,
                        userDetailsDTO.getUserDTO().getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );

        log.info("memberSecurityDTO: {}", memberSecurityDTO);

        return memberSecurityDTO;
    }
}

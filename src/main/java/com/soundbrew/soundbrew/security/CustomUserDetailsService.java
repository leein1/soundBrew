package com.soundbrew.soundbrew.security;

import com.soundbrew.soundbrew.dto.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final PasswordEncoder passwordEncoder;

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

        log.info("이메일 기반 정보 조회(username - 시큐리티),{}", username);

        //  우리가 작성한 User 클래스 아님 - 테스트용
        //  스프링 시큐리티 API에서 UserDetails 를 구현한 User 클래스(builder 지원)를 제공
        UserDetails userDetails = User.builder()
                .username("user1")
//                .password("1111") 엔코더 사용으로 주석
                .password(passwordEncoder.encode("111"))
                .authorities("ROLE_USER")
                .build();

        return userDetails;
    }
}

package com.soundbrew.soundbrew.security;

import com.soundbrew.soundbrew.dto.user.MemberSecurityDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.repository.role.RoleRepository;
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

//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("---------------이메일 기반 정보 조회(username - 유저 엔티티의 email) , {}", username);

        //  실제 유저 정보 이메일로 조회
        //  매개변수로 들어가는 username은 login.html 폼에서 제출한 username이다 -> email
        UserDetailsDTO userDetailsDTO = userRepository.findUserDetailsByEmail(username).orElseThrow();

        log.info(userDetailsDTO);

        String roleType = roleRepository.findById(userDetailsDTO.getUserRoleDTO().getRoleId()).orElseThrow().getRoleType();

        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleType));

        userDetailsDTO.setAuthorities(grantedAuthorities);

        return userDetailsDTO;

        //  우리가 작성한 User 클래스 아님 - 테스트용
        //  스프링 시큐리티 API에서 UserDetails 를 구현한 User 클래스(builder 지원)를 제공
//        UserDetails userDetails = User.builder()
//                .username("user1")
//                .password(passwordEncoder.encode("111"))
//                .roles("USER") // "ROLE_" 접두어를 자동으로 추가
//                .build();

        /*  변경
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

         */


    }
}

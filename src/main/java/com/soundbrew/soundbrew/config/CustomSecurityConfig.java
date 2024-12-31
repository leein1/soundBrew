//package com.soundbrew.soundbrew.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@Log4j2
//@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class CustomSecurityConfig {
//
//
//    //  아무 설정도 하지 않을 경우 모든 사용자 -> 모든 경로 접근 가능
//    //  접근 제어 역할로 유추 & 웹 Filter 공부 필요
//    //  필터들이 단계별로 동작하므로 로그 설정 최대한 낮게 하여 에러 식별 가능하게 설정 -> application.properties
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////
////        log.info("----------------------------------- 시큐리티 설정 -----------------------------------------");
////
////        http.formLogin();
////
////        return http.build();
////    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        log.info("----------------------------------- 시큐리티 설정 -----------------------------------------");
//
//        http
//                .authorizeRequests()
//                .antMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스는 인증 없이 접근 가능
//                .anyRequest().authenticated() // 나머지 요청은 인증 필요
//                .and()
//                .formLogin().and().csrf().disable(); // 기본 로그인 폼 사용
//
//        return http.build();
//    }
//
////    //  정적 자원 시큐리티 적용 x
////    // 브라우저에서 /css/admin.css 정적자원 직접 호출시 로그에서 No security for GET /css/admin.css 출력됨
////    @Bean
////    public WebSecurityCustomizer webSecurityCustomizer() {
////
////        log.info("----------------------------------- 웹 설정 -----------------------------------------");
////
////        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
////    }
//
//
//
//
//
//    //  @RequiredArgsContructor 어노테이션으로도 작동 하는지 확인 필요
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//
//
//}
//
//

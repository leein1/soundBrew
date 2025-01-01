package com.soundbrew.soundbrew.config;

import com.soundbrew.soundbrew.handler.APILoginSuccessHandler;
import com.soundbrew.soundbrew.handler.Custom403Handler;
import com.soundbrew.soundbrew.security.CustomUserDetailsService;
import com.soundbrew.soundbrew.security.filter.APILoginFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {


    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    /*
    CustomuserDetailsService 클래스와 순환참조 문제로 해당 빈 등록 PasswordEncoderConfig 클래스로 분리함

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     */

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    //  403 예외 처리를 위한 핸들러 주입
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {

        //  AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);

        //  Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //  필수!!!
        http.authenticationManager(authenticationManager);

        // API 로그인 필터
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        //  API LoginSuccessHandler 성공시 핸들러 설정
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler();

        // SuccessHandler 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        //  API 로그인필터 위치 조정
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // HttpSecurity 객체를 통해 보안 정책을 설정합니다.
        http.authorizeRequests() // 요청 경로별 인증 및 권한 설정 시작
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // 정적 리소스에 대한 요청은 인증 없이 접근 가능하도록 설정
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Swagger 경로 접근 허용
                .antMatchers("/register").permitAll()
                //  회원가입 경로 접근 허용
                .antMatchers("/api/users/**").permitAll()
                // 회원가입 rest요청 허용
                .antMatchers("/").permitAll()
                //  메인 페이지 경로 접근 허용
                .antMatchers("/files/**").permitAll()
                .antMatchers("/api/sample/**").permitAll()
                .antMatchers("/myInfo").permitAll()
                // '/myInfo' 경로는 'ROLE_USER' 권한을 가진 사용자만 접근 가능
                .anyRequest().authenticated()
                // 위에서 명시적으로 설정되지 않은 모든 요청은 인증이 필요
                .and()
                .formLogin() // 폼 기반 로그인 설정
                .loginPage("/login")
                // 사용자 정의 로그인 페이지 경로 지정 ('/member/login' 사용)
                .permitAll()
                // 로그인 페이지와 관련된 모든 요청은 인증 없이 접근 가능
                .and()
                .logout() // 로그아웃 설정
                .permitAll();
                 // 로그아웃 경로는 인증 여부와 관계없이 누구나 접근 가능

        http.csrf().disable();
        //csrf 비활성

        //  token 사용으로 세션 사용하지 않도록 지정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
        remember-me 쿠키 생성시 쿠키 값 인코딩 위한 key 와 필요한 정보를 저장하는 tokenRepository를 지정해야 한다
        아래의 경우 토큰 지정에 persistentTokenRepository() 를 만들어서 사용

        로그인 화면(html)에서도 remember-me라는 name 속성이 전달 되어야 기능함.
         */
        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(customUserDetailsService)
                .tokenValiditySeconds(60*60*24*30); //30일

        //  403 예외 처리
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        return http.build();
        // 설정한 보안 정책을 기반으로 SecurityFilterChain 객체를 생성하여 반환
    }


    /*



    아무 설정도 하지 않을 경우 모든 사용자 -> 모든 경로 접근 가능
    접근 제어 역할로 유추 & 웹 Filter 공부 필요
    필터들이 단계별로 동작하므로 로그 설정 최대한 낮게 하여 에러 식별 가능하게 설정 -> application.properties
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("----------------------------------- 시큐리티 설정 -----------------------------------------");

        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스는 인증 없이 접근 가능
                .and().formLogin().loginPage("/member/login").permitAll();
        // 로그인 페이지와 관련된 모든 요청은 인증 없이 접근 가능


        return http.build();
    }

     */

/*
    2.7 이상 버전부터 스프링에서 권장하지 않는 방법이다.
    http 객체 설정시  .authorizeRequests() 사용 방식을 권장


    //  정적 자원 시큐리티 적용 x
    // 브라우저에서 /css/admin.css 정적자원 직접 호출시 로그에서 No security for GET /css/admin.css 출력됨
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("----------------------------------- 웹 설정 -----------------------------------------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

 */









}



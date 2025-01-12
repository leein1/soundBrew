package com.soundbrew.soundbrew.config;

import com.soundbrew.soundbrew.security.filter.RefreshTokenFilter;
import com.soundbrew.soundbrew.security.handler.APILoginSuccessHandler;
import com.soundbrew.soundbrew.handler.Custom403Handler;
import com.soundbrew.soundbrew.security.CustomUserDetailsService;
import com.soundbrew.soundbrew.security.filter.APILoginFilter;
import com.soundbrew.soundbrew.security.filter.TokenCheckFilter;
import com.soundbrew.soundbrew.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;

    //  AccessDeniedHandler 빈 등록
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    // TokenCheckFilter 생성
    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil){
        return new TokenCheckFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {

        //  AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        // API 로그인 필터
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // 필터 순서
//        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), UsernamePasswordAuthenticationFilter.class); // 가장 먼저 실행
//        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class); // 로그인 필터
//        http.addFilterAfter(tokenCheckFilter(jwtUtil), APILoginFilter.class); // 액세스 토큰 검증은 로그인 필터 이후

//        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(tokenCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), TokenCheckFilter.class);


        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), UsernamePasswordAuthenticationFilter.class); // 리프레시 토큰 처리
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class); // 로그인 처리
        http.addFilterAfter(tokenCheckFilter(jwtUtil), APILoginFilter.class); // 액세스 토큰 검증

        // HttpSecurity 설정
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                .antMatchers("/generateToken",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/register",
                        "/api/users/**",
                        "/",
                        "/files/**",
                        "/api/**",  // 모든 API 요청을 인증 없이 허용
                        "/api/sample/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin().loginPage("/login").permitAll()

                .and()
                .logout().permitAll();

        // 요청 경로별 권한 설정
//        http.authorizeHttpRequests((requests) -> requests
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .requestMatchers("/generateToken", "/swagger-ui/**", "/v3/api-docs/**", "/register", "/api/users/**", "/", "/files/**", "/api/sample/**").permitAll()
//                .requestMatchers("/api/me").authenticated()
//                .anyRequest().authenticated()
//        );

        //  csrf 비활성 / 세션 비활성
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //  403 예외 처리
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }


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



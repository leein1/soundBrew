package com.soundbrew.soundbrew.config;

import com.soundbrew.soundbrew.security.filter.RefreshTokenFilter;
import com.soundbrew.soundbrew.security.handler.APILoginFailureHandler;
import com.soundbrew.soundbrew.security.handler.APILoginSuccessHandler;
import com.soundbrew.soundbrew.handler.Custom403Handler;
import com.soundbrew.soundbrew.security.CustomUserDetailsService;
import com.soundbrew.soundbrew.security.filter.APILoginFilter;
import com.soundbrew.soundbrew.security.filter.TokenCheckFilter;
import com.soundbrew.soundbrew.security.JWTUtil;
import com.soundbrew.soundbrew.service.RoleService;
import com.soundbrew.soundbrew.service.user.UserService;
import com.soundbrew.soundbrew.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;
    private final PublicPathsProperties publicPathsProperties;
    private final MaintenanceConfig maintenanceConfig;
    private final UserService userService;
    private final RoleService roleService;

    // 모든 출처에서 CORS를 허용하는 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 출처 허용
        configuration.setAllowedOrigins(List.of("*"));  // "*"은 모든 출처를 허용

        // 모든 HTTP 메서드 허용
        configuration.setAllowedMethods(List.of("*"));  // "*"은 모든 HTTP 메서드를 허용

        // 모든 헤더 허용
        configuration.setAllowedHeaders(List.of("*"));  // "*"은 모든 헤더를 허용

        // 자격 증명 허용 여부
        configuration.setAllowCredentials(false);  // 자격 증명(Cookie 등)을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정
        return source;
    }

    //  AccessDeniedHandler 빈 등록
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    // TokenCheckFilter 생성
    private TokenCheckFilter tokenCheckFilter(){
        return new TokenCheckFilter(jwtUtil,customUserDetailsService,publicPathsProperties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {

        //  AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        //  유지보수 상태 확인
        http.addFilterBefore((request,response,filterChain) -> {

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            if(maintenanceConfig.isMode()){
                httpServletResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                httpServletResponse.setHeader("Retry-After", "3600");
                httpServletResponse.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정
                httpServletResponse.setContentType("text/plain; charset=UTF-8"); // Content-Type 설정
                httpServletResponse.getWriter().print("서버가 유지 보수 중입니다. 나중에 다시 시도 해주세요.");
                return;
            }

            filterChain.doFilter(request, response);

        }, UsernamePasswordAuthenticationFilter.class);

        // API 로그인 필터
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // 인증 성공 핸들러
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // 인증 실패 핸들러
        APILoginFailureHandler failureHandler = new APILoginFailureHandler(jwtUtil);
        apiLoginFilter.setAuthenticationFailureHandler(failureHandler);


        //  필터 순서 지정
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil,userService,roleService), UsernamePasswordAuthenticationFilter.class); // 리프레시 토큰 처리
        http.addFilterAfter(apiLoginFilter, RefreshTokenFilter.class); // 로그인 처리
        http.addFilterAfter(tokenCheckFilter(), APILoginFilter.class); // 액세스 토큰 검증


        // HttpSecurity 설정
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .antMatchers(publicPaths().toArray(new String[0])).permitAll()  // 인증 불필요 경로
                .antMatchers(publicPathsProperties.getPaths().toArray(new String[0])).permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()

                .and()
                .formLogin().loginPage("/login").permitAll()

                .and()
                .logout().permitAll();

        //  csrf 비활성 / 세션 비활성
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //  403 예외 처리
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        return http.build();
    }

//    private List<String> publicPaths(){
//        return List.of(
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/fonts/**",
//                "/generateToken",
//                "/register",
//                "/api/users/**",
//                "/",
//                "/api/admin/tags",
//                "/test-s3",
//                "/me/**",
//                "/api/me/**",
//                "/files/**",
//                "/sounds/**",
//                "/api/sounds/**",
//                "/stream/**",
//                "/api/admin/**",
//                "/admin/**",
////                        "/api/**",  // 모든 API 요청을 인증 없이 허용
//                "/myInfo",
//                "/api/sample/**"
//        );
//    }

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



package com.soundbrew.soundbrew.security.filter;

import com.soundbrew.soundbrew.config.PublicPathsProperties;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.security.CustomUserDetailsService;
import com.soundbrew.soundbrew.security.exception.AccessTokenException;
import com.soundbrew.soundbrew.security.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    //        log.info("Token Check Filter JWTUtil:{}", jwtUtil);

    private final CustomUserDetailsService customUserDetailsService;
    private final PublicPathsProperties publicPathsProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/fonts/")
                || path.startsWith("/favicon.ico");
    }

    private boolean isPublicPath(String path) {
        log.info("공개 경로 불러오기: {}", publicPathsProperties.getPaths());

        List<String> publicPaths = publicPathsProperties.getPaths();

        for (String publicPath : publicPaths) {
            boolean matched = new AntPathMatcher().match(publicPath, path);
            if (matched) {
                log.info("공개 경로에 포함되어 있음: {}", path);
                return true;
            }
        }
        return false;
    }

    private Map<String,Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        log.info("-------------------------------------Token Check Filter.validateAccessToken-----------------------");

        String headerStr = request.getHeader("Authorization");
        log.info("-------------headerStr {}", headerStr);

        if(headerStr == null){

            log.error("-------------headerStr NULL");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);

        } else if(headerStr.length() < 8){

            log.error("-------------headerStr length less than 8");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);
        log.info("tokenType : {}, tokenStr : {}", tokenType,tokenStr);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String,Object> values = jwtUtil.validateToken(tokenStr);

            log.info("TokenCheckFilter.validateAccessToken() 실행 문제 없음");
            log.info("TokenCheckFilter.validateAccessToken values : {}", values);

            return values;

        }catch (MalformedJwtException malformedJwtException){

            log.error("MalformedJwtException-----------------------------------------------------------------");
            log.error("MalformedJwtException: {}", malformedJwtException.getMessage());
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);

        }catch (SignatureException signatureException){

            log.error("SignatureException-----------------------------------------------------------------");
            log.error("SignatureException: {}", signatureException.getMessage());
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);

        }catch (ExpiredJwtException expiredJwtException){

            log.error("ExpiredJwtException-----------------------------------------------------------------");
            log.error("ExpiredJwtException: {}", expiredJwtException.getMessage());
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);

        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("-------------------------------------Token Check Filter.doFilterInternal-----------------------");

        String path = request.getRequestURI();
        log.info("Token Check Filter에서 해당 경로 검증 수행 : {}", path);

        if(!path.startsWith("/api/")){

            log.info("request 요청이 /api 가 아님: {} ", path);
            filterChain.doFilter(request, response);
            return;
        }

        if(isPublicPath(path)){

            log.info("TokenCheckFilter 제외 경로이다 :{}", path);
            filterChain.doFilter(request,response);

            return;
        }

        try{

            //  전달받은 토큰 검증
            Map<String,Object> values = this.validateAccessToken(request);

            log.info("Token Check Filter 인증 정보 생성 시작, 전달받은 토큰 내용: {}", values.toString());

            /*
              npe 방지를 위해 찾으려는 문자열.equals(가져온 값)의 형태로 작성
              가져온 값.equals(찾으려는 문자열)의 형태일시
              type의 값이 null일때 null.equals(찾으려는 문자열)이 되어 npe 발생 할거 같음
             */

            UsernamePasswordAuthenticationToken authentication;

            // 비밀번호 재발급용 토큰일 경우 "type":"password_reset" 이 맞는지
            if(values.containsKey("type") && "password_reset".equals(values.get("type"))){

                String username = (String) values.get("username");

                List<GrantedAuthority> authorities= new ArrayList<>();;
                authorities.add(new SimpleGrantedAuthority("PASSWORD_RESET"));

                //  인증 정보 생성
                authentication = new UsernamePasswordAuthenticationToken(
                        UserDetailsDTO.builder().
                                userDTO(
                                        UserDTO.builder()
                                        .email(username)
                                        .build())
                                .build(),
                        null,
                        authorities
                );

            } else {

                // username roles 추출
                String email = (String) values.get("username");
                int userId = (int) values.get("userId");
                String nickname = (String) values.get("nickname");
                String profileImagePath = (String) values.get("profileImagePath");
                int subscriptionId = (int) values.get("subscriptionId");
                List<String> roles = (List<String>) values.get("roles");

                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                //  인증 정보 생성
                authentication = new UsernamePasswordAuthenticationToken(
                        UserDetailsDTO.builder().
                                userDTO(
                                        UserDTO.builder()
                                        .userId(userId)
                                        .email(email)
                                        .nickname(nickname)
                                        .profileImagePath(profileImagePath)
                                        .subscriptionId(subscriptionId)
                                        .build())
                                .build(),
                        null,
                        authorities
                );
            }

            //  SecurityContextHolder 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("SecurityContext set");

            //  SecurityConfig 다음 실행
            filterChain.doFilter(request, response);

        }catch (AccessTokenException accessTokenException){

            log.error("Token 검증 실패: {}", accessTokenException.getMessage());
            log.error(accessTokenException);
            log.error(accessTokenException.getMessage());

            SecurityContextHolder.clearContext(); // 인증 실패 시 컨텍스트 초기화
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // 401 응답 반환
            accessTokenException.sendResponseError(response);
        }
    }




}

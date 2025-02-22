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

    /*
    현재 사용자가 로그인한 사용자인지 체크하는 체크용 필터와 유사하게 JWT 토큰을 검사하는 역할

    OncePerRequestFilter를 상속해서 구성하며 해당 필터는 하나의 요청에 대해 한번씩 동작하는 필터 -> 서블릿 API의 필터와 유사

    설정은 CustomSecurityConfig에서 지정
     */

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final PublicPathsProperties publicPathsProperties;

    //  토큰 검증 필요 없는 경로
//    private static final List<String> EXCLUDE_PATHS = List.of(
//            "/api/sounds/tracks",
//            "/api/sounds/tags"
//    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/fonts/")
                || path.startsWith("/favicon.ico");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("-------------------------------------Token Check Filter.doFilterInternal-----------------------");

        String path = request.getRequestURI();
        log.info("Token Check Filter requestPath : {}", path);

//        // 제외 리스트에 포함된 경로는 통과
//        if (EXCLUDE_PATHS.contains(path)) {
//
//            log.info("TokenCheckFilter 제외 요청 경로 : {}", path);
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        if(!path.startsWith("/api/")){
//
//            log.info("request 요청이 /api가 아님 ");
//            filterChain.doFilter(request, response);
//
//            return;
//        }

        if(!path.startsWith("/api/")){
//
            log.info("request 요청이 /api가 아님 ");
            filterChain.doFilter(request, response);

            return;
        }

        if(isPublicPath(path)){

            log.info("TokenCheckFilter 제외 경로 :{}", path);
            filterChain.doFilter(request,response);

            return;
        }

        log.info("Token Check Filter JWTUtil:{}", jwtUtil);


        try{

            //  전달받은 토큰 검증
            Map<String,Object> values = validateAccessToken(request);

            log.info("Token Check Filter username - 인증 정보 생성 시작");

            /*
              npe 방지를 위해 문자열.equals(가져온 값)의 형태로 작성
              가져온값.equals(찾으려는 문자열)의 형태일시 type이라는 키는 있지만 값이 null일때 null.equals(찾으려는 문자열)이 되어 npe 발생 할거 같음
             */

            UsernamePasswordAuthenticationToken authenticationToken;

            if(values.containsKey("type") && "password_reset".equals(values.get("type"))){


                String username = (String) values.get("username");

                List<GrantedAuthority> authorities= new ArrayList<>();;
                authorities.add(new SimpleGrantedAuthority("PASSWORD_RESET"));

                //  인증 정보 생성
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        UserDetailsDTO.builder().
                                userDTO(UserDTO.builder()
                                        .email(username)
                                        .build())
                                .build(),
                        null,
                        authorities
                );

            } else {

                // username roles 추출
                String username = (String) values.get("username");
                int userId = (int) values.get("userId");
                String nickname = (String) values.get("nickname");
                String profileImagePath = (String) values.get("profileImagePath");
                int subscriptionId = (int) values.get("subscriptionId");
                List<String> roles = (List<String>) values.get("roles");

                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                //  인증 정보 생성
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        UserDetailsDTO.builder().
                                userDTO(UserDTO.builder()
                                        .userId(userId)
                                        .email(username)
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
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("SecurityContext set");

            //  SecurityConfig 다음 실행
            filterChain.doFilter(request, response);

        }catch (AccessTokenException accessTokenException){



            //  메시지를 보내는 것이 아닌 컨텍스트 초기화 후 401 에러 반환
            log.error("Token 검증 실패: {}", accessTokenException.getMessage());
            log.error(accessTokenException);
            log.error(accessTokenException.getMessage());

            SecurityContextHolder.clearContext(); // 인증 실패 시 컨텍스트 초기화
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // 401 응답 반환
            accessTokenException.sendResponseError(response);
        }
    }

    private boolean isPublicPath(String path) {
        log.info("Public paths loaded: {}", publicPathsProperties.getPaths());

        List<String> publicPaths = publicPathsProperties.getPaths();

        for (String publicPath : publicPaths) {
            boolean matched = new AntPathMatcher().match(publicPath, path);
//            log.info("Checking match: {} <-> {}, result: {}", publicPath, path, matched);
            if (matched) {
                log.info("Public path match: {}", path);
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
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MARFORM);

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


}

package com.soundbrew.soundbrew.security.filter;

import com.soundbrew.soundbrew.security.exception.AccessTokenException;
import com.soundbrew.soundbrew.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    /*`
    현재 사용자가 로그인한 사용자인지 체크하는 체크용 필터와 유사하게 JWT 토큰을 검사하는 역할

    OncePerRequestFilter를 상속해서 구성하며 해당 필터는 하나의 요청에 대해 한번씩 동작하는 필터 -> 서블릿 API의 필터와 유사

    설정은 CustomSecurityConfig에서 지정
     */

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("-------------------------------------Token Check Filter.doFilterInternal-----------------------");

        String path = request.getRequestURI();
        log.info("Token Check Filter requestPath : {}", path);

        if(!path.equals("/myInfo") ){

//            !path.startsWith("/api/") &&

                    log.info("request 요청이 /api가 아님 ");

            filterChain.doFilter(request, response);
            return;
        }


        log.info("Token Check Filter JWTUtil: {}", jwtUtil);


        try{

            //  전달받은 토큰 검증
            Map<String,Object> values = validateAccessToken(request);

            // username roles 추출
            String username = (String) values.get("username");
            List<String> roles = (List<String>) values.get("roles");

            log.info("Token Check Filter username - 인증 정보 생성 시작");


            //  인증 정보 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );

            //  SecurityContextHolder 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("SecurityContext set");

            //  SecurityConfig 다음 실행
            filterChain.doFilter(request, response);

        }catch (AccessTokenException accessTokenException){

//            accessTokenException.sendResponseError(response);

            //  메시지를 보내는 것이 아닌 컨텍스트 초기화 후 401 에러 반환
            log.error("Token 검증 실패: {}", accessTokenException.getMessage());
            SecurityContextHolder.clearContext(); // 인증 실패 시 컨텍스트 초기화
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // 401 응답 반환
        }
    }

    private Map<String,Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {

        log.info("-------------------------------------Token Check Filter.validateAccessToken-----------------------");

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length() < 8){
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

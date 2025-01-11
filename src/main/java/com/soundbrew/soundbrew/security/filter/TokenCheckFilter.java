package com.soundbrew.soundbrew.security.filter;

import com.soundbrew.soundbrew.security.exception.AccessTokenException;
import com.soundbrew.soundbrew.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    /*
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

        if(!path.startsWith("/api/")){

            log.info("request 요청이 /api가 아님 ");

            filterChain.doFilter(request, response);
            return;
        }


        log.info("Token Check Filter JWTUtil: {}", jwtUtil);


        try{

            validateAccessToken(request);
            filterChain.doFilter(request, response);

        }catch (AccessTokenException accessTokenException){

            accessTokenException.sendResponseError(response);
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

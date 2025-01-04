package com.soundbrew.soundbrew.security.filter;

import com.google.gson.Gson;
import com.soundbrew.soundbrew.security.exception.RefreshTokenException;
import com.soundbrew.soundbrew.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.equals(refreshPath)) {
            log.info("skip refresh token filter....");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Refresh token filter....Run............");

        //  Json에서 accessToken + refreshToken 가져오기
        Map<String,String> tokens = parseRequestJSon(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken : {}",accessToken);
        log.info("refreshToken : {}",refreshToken);

        try{
            checkAccessToken(accessToken);
        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return; // 더 이상 실행 필요x
        }

        Map<String, Object> refreshClaims = null;

        try{
            refreshClaims = checkRefreshToken(refreshToken);
            log.info(refreshClaims);

            //  refreshToken 유효시간이 얼마 안 남았을때
            Integer exp = (Integer) refreshClaims.get("exp");
            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli()* 1000);
            Date current = new Date(System.currentTimeMillis());

            //  만료 시간 현재 시간 간격 계산
            // 3일 미만일 경우 Refresh Token 새로 발급
            long gapTime = (expTime.getTime() - current.getTime());

            log.info("-----------------------------------");
            log.info("current : {}", current);
            log.info("exp : {}", exp);
            log.info("gap : {}", gapTime);

            String username = (String) refreshClaims.get("username");

            //  여기까지 도달할 경우 무조건 AccessToken새로 발급
            String accessTokenValue = jwtUtil.generateToken(Map.of("username",username),1);
            String refreshTokenValue = tokens.get("refreshToken");

            // refreshToken이 3일도 안 남은 경우
            if(gapTime < (1000* 60 * 60 * 24 * 3)){
                log.info("new Refresh Token Required.....");
                refreshTokenValue = jwtUtil.generateToken(Map.of("username",username),30);
            }

            log.info("Refresh Token Success.....");
            log.info("accessTokenValue : {}", accessTokenValue);
            log.info("refreshTokenValue : {}", refreshTokenValue);


        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
            return; // 더 이상 실행할 코드 x
        }
    }

    private Map<String, String> parseRequestJSon(HttpServletRequest request) {

        //  Json 분석 후 email,password 전달 값을 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())) {

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException {

        try{
            jwtUtil.validateToken(accessToken);
        }catch (ExpiredJwtException expiredJwtException){
            log.info("Access Token has expired");
        }catch (Exception exception){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String,Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {

        try{
            Map<String,Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        } catch (ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException malformedJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch (Exception exception){
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }

        return null;
    }

    

}

package com.soundbrew.soundbrew.security.filter;

import com.google.gson.Gson;
import com.soundbrew.soundbrew.security.exception.RefreshTokenException;
import com.soundbrew.soundbrew.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
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

        log.info("--------------------------------------------RefreshTokenFilter.dofilterInternal ---------------------");
        String path = request.getRequestURI();

        if(!path.equals(refreshPath)) {
            log.info("요청 경로가 refreshPath가 아님 - 요청 경로 : {}" + path);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("RefreshTokenFilter 실행 ");

        //  Json에서 accessToken + refreshToken 가져오기
        Map<String,String> tokens = parseRequestJSon(request);

        /*
        아래 코드에서 null일 경우 NPE 발생 수정 필요
         */
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("RefreshTokenFilter.dofilterInternal accessToken : {}",accessToken);
        log.info("RefreshTokenFilter.dofilterInternal refreshToken : {}",refreshToken);

        try{
            log.info("RefreshTokenFilter.dofilterInternal - checkAccessToken() 실행");
            checkAccessToken(accessToken);

        }catch (RefreshTokenException refreshTokenException){

            refreshTokenException.sendResponseError(response);

        }

        Map<String, Object> refreshClaims = null;

        try{
            log.info("RefreshTokenFilter.dofilterInternal - checkRefreshToken() 실행");

            refreshClaims = checkRefreshToken(refreshToken);
            log.info("checkRefreshToken() 결과 : {}", refreshClaims);

            //  refreshToken 유효시간이 얼마 안 남았을때
            Integer exp = (Integer) refreshClaims.get("exp");
            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli()* 1000);
            Date current = new Date(System.currentTimeMillis());

            //  만료 시간 현재 시간 간격 계산
            // 만약 3일 미만일 경우 Refresh Token도 새로 발급
            long gapTime = (expTime.getTime() - current.getTime());

            log.info("-----------------------------------");
            log.info("current : {}", current);
            log.info("exp : {}", exp);
            log.info("gap : {}", gapTime);

            String username = (String) refreshClaims.get("username");

            //  여기까지 도달할 경우 무조건 AccessToken새로 발급
            log.info("AccessToken 새로 발급");
            String accessTokenValue = jwtUtil.generateToken(Map.of("username",username),1);
            String refreshTokenValue = tokens.get("refreshToken");

            // refreshToken이 3일도 안 남은 경우
            if(gapTime < (1000* 60 * 60 * 24 * 3)){
                log.info("RefreshToken 유효기간 3일 미만 - 새로 발급");
                refreshTokenValue = jwtUtil.generateToken(Map.of("username",username),30);
            }

            log.info("Refresh Token 종료.....");
            log.info("accessTokenValue : {}", accessTokenValue);
            log.info("refreshTokenValue : {}", refreshTokenValue);

            sendTokens(accessTokenValue,refreshTokenValue,response);

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

            log.info("RefreshTokenFilter.checkAceessToken : {}", accessToken);

            jwtUtil.validateToken(accessToken);

        }catch (ExpiredJwtException expiredJwtException){

            log.info("RefreshTokenFilter.checkAccessToken : Access 토큰 만료됨. 정상 흐름이라 로그만 출력");

        }catch (Exception exception){

            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String,Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {

        try{

            log.info("RefreshTokenFilter.checkRefreshToken : {}", refreshToken);

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

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response){

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,"refreshToken",refreshTokenValue));

        try{

            response.getWriter().println(jsonStr);

        }catch (IOException e){

            throw new RuntimeException(e);
        }
    }


}

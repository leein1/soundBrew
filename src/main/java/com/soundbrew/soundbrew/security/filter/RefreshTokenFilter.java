package com.soundbrew.soundbrew.security.filter;

import com.google.gson.Gson;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.security.exception.RefreshTokenException;
import com.soundbrew.soundbrew.security.JWTUtil;
import com.soundbrew.soundbrew.service.RoleService;
import com.soundbrew.soundbrew.service.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/fonts/")
                || path.startsWith("/favicon.ico");
    }

    private Map<String, String> parseRequestJSon(HttpServletRequest request) {

        //  Json 분석 후 전달 값을 Map으로 처리
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

            log.info("RefreshTokenFilter.checkAceessToken accessToken 검증 실행: {}", accessToken);

            jwtUtil.validateToken(accessToken);

        }catch (ExpiredJwtException expiredJwtException){

            log.info("RefreshTokenFilter.checkAccessToken : Access 토큰 만료됨. 정상 흐름");

        }catch (Exception exception){

            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String,Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {

        try{

            log.info("RefreshTokenFilter.checkRefreshToken 검증 실행 refreshToken 검증 실행: {}", refreshToken);

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

    private Map<String,Object> createJwtClaims(String email){


        UserDTO userDTO = userService.getUserByEmail(email).getDto();
        int userId =  userDTO.getUserId();

        UserDetailsDTO userDetailsDTO = userService.getUserWithDetails(userId).getDto();

        String roleType = roleService.getRoleTypeById(userDetailsDTO.getUserRoleDTO().getRoleId());

        List<GrantedAuthority> grantedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + roleType)
        );

        List<String> roles = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        //  JWTUtil에 전달될 valueMap

        Map<String,Object> claim = Map.of(
                "username", userDTO.getEmail(),
                "userId", userDTO.getUserId(),
                "nickname", userDTO.getNickname(),
                "profileImagePath", userDTO.getProfileImagePath(),
                "subscriptionId", userDTO.getSubscriptionId(),
                "roles", roles
        );

        return claim;
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response){

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();

        String jsonStr = gson.toJson(
                Map.of(
                        "accessToken", accessTokenValue,
                        "refreshToken",refreshTokenValue
                )
        );

        try{

            response.getWriter().println(jsonStr);

        }catch (IOException e){

            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("--------------------------------------------RefreshTokenFilter.dofilterInternal ---------------------");
        String path = request.getRequestURI();

        if(!path.equals(refreshPath)) {

            log.info("요청 경로가 /refreshToken이 아님: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("RefreshTokenFilter 실행 ");

        //  Json에서 accessToken + refreshToken 가져오기
        Map<String,String> tokens = this.parseRequestJSon(request);

        /*
        아래 코드에서 대입한 값이 null일 경우
        accessToken 이 null인 경우 checkAccessToken() 내부에서 RefreshTokenException.ErrorCase.NO_ACCESS 를 던지고 종료
        refreshToken에 문제가 있는 경우 checkRefreshToken() 내부에서 상황에 맞는 예외를 던지며 종료
         */
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("RefreshTokenFilter.dofilterInternal accessToken : {}",accessToken);
        log.info("RefreshTokenFilter.dofilterInternal refreshToken : {}",refreshToken);

        try{

            log.info("RefresvhTokenFilter.dofilterInternal - checkAccessToken() 실행");
            this.checkAccessToken(accessToken);

        }catch (RefreshTokenException refreshTokenException){

            refreshTokenException.sendResponseError(response);

        }

        Map<String, Object> refreshClaims = null;

        try{
            log.info("RefreshTokenFilter.dofilterInternal - checkRefreshToken() 실행");

            // checkRefreshToken()에서 검증 후 각 k,v를 Map 으로 반환해줌
            refreshClaims = this.checkRefreshToken(refreshToken);
            log.info("checkRefreshToken() 결과 : {}", refreshClaims);

            /*
             refreshToken 유효시간이 얼마 안 남았을때
             jjwt에서 토큰을 변환할때 exp르 초단위로 변환함
             Date 는 밀리초를 사용
             */
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

            String email = (String) refreshClaims.get("username");

            Map<String,Object> claim = this.createJwtClaims(email);

            /**
             *  여기까지 도달할 경우 무조건 AccessToken새로 발급
             */
            log.info("AccessToken 새로 발급");
            String accessTokenValue = jwtUtil.generateToken(claim,4);
            String refreshTokenValue = tokens.get("refreshToken");

            /**
             * refreshToken이 3일도 안 남은 경우
             */
            if(gapTime < (1000* 60 * 60 * 24 * 3)){
                log.info("RefreshToken 유효기간 3일 미만 - 새로 발급");
                refreshTokenValue = jwtUtil.generateToken(Map.of("username",email),30);
            }

            log.info("Refresh Token 종료.....");
            log.info("NEW accessTokenValue : {}", accessTokenValue);
            log.info("NEW refreshTokenValue : {}", refreshTokenValue);

            this.sendTokens(accessTokenValue,refreshTokenValue,response);

        }catch (RefreshTokenException refreshTokenException){

            refreshTokenException.sendResponseError(response);
            return; // 더 이상 실행할 코드 x
        }
    }




}


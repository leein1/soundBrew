package com.soundbrew.soundbrew.security.handler;

import com.google.gson.Gson;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
     
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------Login Success Handler ------------------------------");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);
        log.info(authentication.getName());

        //  권한 정보 가져오기
        List<String> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        //user 정보 가져오기
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        int userId = userDetails.getUserDTO().getUserId();
        String nickname = userDetails.getUserDTO().getNickname();
        String profileImagePath = userDetails.getUserDTO().getProfileImagePath();
        int subscriptionId = userDetails.getUserDTO().getSubscriptionId();

        //  JWTUtil에 전달될 valueMap
        Map<String,Object> claim = Map.of(
                "username", authentication.getName(),
                "userId", userId,
                "nickname", nickname,
                "profileImagePath", profileImagePath,
                "subscriptionId", subscriptionId,
                "roles", roles
        );

        // Access Token 기간 1일
        String accessToken = jwtUtil.generateToken(claim,4);
        //  Refresh Token 기간 30일
        String refreshToken = jwtUtil.generateToken(claim,30);

        //  로그인 성공시 응답 - 확인을 위해 userId, nickname 정보도 같이 응답으로 보냄
        Map<String,String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "redirectUrl", "/sounds/tracks"
        );

        Gson gson = new Gson();

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().print(jsonStr);
    }
}

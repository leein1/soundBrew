package com.soundbrew.soundbrew.security.handler;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
public class APILoginFailureHandler implements AuthenticationFailureHandler {
        //
        // 이거 새로 만듦
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("----------------------------Login Failure Handler ------------------------------");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 에러 메시지를 JSON 형태로 응답
        Map<String,String> keyMap = Map.of(
            "message", "로그인 실패",
            "redirectUrl", "/login"
        );

        Gson gson = new Gson();

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().print(jsonStr);
    }
}

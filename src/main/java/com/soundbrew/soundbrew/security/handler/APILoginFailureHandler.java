package com.soundbrew.soundbrew.security.handler;

import com.google.gson.Gson;
import com.soundbrew.soundbrew.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginFailureHandler implements AuthenticationFailureHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("----------------------------Login Failure Handler ------------------------------");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String message = "";
        String redirectUrl = "/login";
        String resetToken = "";

        if(exception instanceof LockedException){

            message = "계정이 잠겨 있습니다.";

        } else if(exception instanceof DisabledException){

            message = "계정이 비활성화되었습니다.";

        } else if(exception instanceof AccountExpiredException){

            message = "계정이 만료되었습니다.";

        } else if(exception instanceof CredentialsExpiredException){

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            message = "비밀번호가 만료되었습니다. 비밀번호를 변경해주세요.";

            // 요청 속성에서 username을 추출
            String username = (String) request.getAttribute("username");

            // 재설정용 토큰 발급
            Map<String, Object> claim = Map.of(
                    "username", username,
                    "type", "password_reset"
            );

            // 예를 들어, 유효기간 15분
            resetToken = jwtUtil.generateTokenWithMinutes(claim, 15);


            redirectUrl = "/help/reset-password";

        } else if(exception instanceof BadCredentialsException){

            message = "아이디 또는 비밀번호가 올바르지 않습니다.";

        } else if(exception instanceof AuthenticationServiceException){

            message = "현재 인증 서비스에 문제가 있습니다. 잠시후 시도하시거나 문의해주세요.";

        }

        // 에러 메시지를 JSON 형태로 응답
        Map<String,String> keyMap = Map.of(
                "message", message,
                "redirectUrl", redirectUrl,
                "resetToken", resetToken
        );


        Gson gson = new Gson();

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().print(jsonStr);
    }
}

package com.soundbrew.soundbrew.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    /*
        해당 핸들러 작동을 위해 CustomSecurityConfig에 빈 처리 + 예외 지정 해줌
     */

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info("------------------------Access Denied---------------------");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        //  json요청이었는지 확인
        String contentType = request.getHeader("content-type");
        /*
        contains()는 application/json 이라는 문자열이 어디에 위치해도 true 반환이기 때문에(json인지 구별하기엔 불명확함)
        startsWith() 사용 - json요청은 contentType이 항상 application/json으로 시작하는 명확한 규칙이 있기 때문에
         */
        boolean jsonRequest = contentType != null && contentType.startsWith("application/json");

        log.info("JSON Request: " + jsonRequest);

        /*
          json이 아닌 요청
          로그인 페이지로 error 파라미터와 함께 리다이렉트
         */
        if(!jsonRequest){
            response.sendRedirect("/login?error=ACCESS_DENIED");
        }

    }


}

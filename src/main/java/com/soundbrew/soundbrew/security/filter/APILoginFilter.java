package com.soundbrew.soundbrew.security.filter;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {


    /*
    AbstractAuthenticationProcessingFilter 는 로그인 처리로 인해 로그인처리 경로와 실제 인증처리를 하는 Authentication-Manager 객체 설정이 필요
    CustomSecurityConfig에서 설정해준다
     */
    public APILoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request) {

        log.info("--------------------------- APILoginFilter.parseRequestJSON ----------------------");
        //  JSON 데이터를 분석, email,password 값 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())) {

            Gson gson = new Gson();

            return gson.fromJson(reader,Map.class);

        } catch (Exception e){

            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("-------------------------API Login Filter.attemptAuthentication ----------------------------");

        if(request.getMethod().equalsIgnoreCase("GET")) {

            log.info("GET 메소드는 지원하지 않음!!!!!!!!");
            return null;
        }

        Map<String,String> jsonData = parseRequestJSON(request);

        log.info("json Data : {}", jsonData);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(jsonData.get("username"), jsonData.get("password"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }
    // 🔹 인증 실패 시 401 응답 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        log.warn("⚠️ 인증 실패: {}", failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Authentication Failed\", \"message\": \"" + failed.getMessage() + "\"}");
        response.getWriter().flush();
    }

}

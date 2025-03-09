package com.soundbrew.soundbrew.security.exception;


import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
//
//public class RefreshTokenException extends RuntimeException {
//
//    private ErrorCase errorCase;
//
//    public enum ErrorCase {
//        NO_ACCESS,
//        BAD_ACCESS,
//        NO_REFRESH,
//        OLD_REFRESH,
//        BAD_REFRESH
//    }
//
//    public RefreshTokenException(ErrorCase errorCase) {
//        super(errorCase.name());
//        this.errorCase = errorCase;
//    }
//
//    public void sendResponseError(HttpServletResponse response) {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        Gson gson = new Gson();
//
//        String responseStr = gson.toJson(Map.of("msg", errorCase.name(), "time", new Date()));
//
//        try{
//            response.getWriter().println(responseStr);
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }
//    }
//
//
//}

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends RuntimeException {

    private final ErrorCase errorCase;

    @Getter
    public enum ErrorCase {
        NO_ACCESS(HttpStatus.UNAUTHORIZED,
                "No access: refresh token permissions are missing.",
                "리프레시 토큰 접근 권한이 없습니다."),
        BAD_ACCESS(HttpStatus.UNAUTHORIZED,
                "Bad access: invalid refresh token usage.",
                "잘못된 리프레시 토큰 접근입니다."),
        NO_REFRESH(HttpStatus.UNAUTHORIZED,
                "No refresh token: refresh token is missing.",
                "리프레시 토큰이 없습니다."),
        OLD_REFRESH(HttpStatus.UNAUTHORIZED,
                "Old refresh token: token is expired.",
                "리프레시 토큰이 만료되었습니다."),
        BAD_REFRESH(HttpStatus.UNAUTHORIZED,
                "Bad refresh token: token is malformed or invalid.",
                "잘못된 리프레시 토큰입니다.");

        private final HttpStatus status;
        private final String developerMessage;
        private final String clientMessage;

        ErrorCase(HttpStatus status, String developerMessage, String clientMessage) {
            this.status = status;
            this.developerMessage = developerMessage;
            this.clientMessage = clientMessage;
        }
    }

    public RefreshTokenException(ErrorCase errorCase) {
        super(errorCase.getDeveloperMessage());
        this.errorCase = errorCase;
    }

    public HttpStatus getStatus() {
        return errorCase.status;
    }

    public String getDeveloperMessage() {
        return errorCase.developerMessage;
    }

    public String getClientMessage() {
        return errorCase.clientMessage;
    }

    public void sendResponseError(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", errorCase.name(), "time", new Date()));

        try{
            response.getWriter().println(responseStr);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

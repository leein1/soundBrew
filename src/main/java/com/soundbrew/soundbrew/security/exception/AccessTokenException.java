package com.soundbrew.soundbrew.security.exception;


import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

//public class AccessTokenException extends RuntimeException {
//
//    TOKEN_ERROR token_error;
//
//    public enum TOKEN_ERROR{
//
//        UNACCEPT(401, "Token is null or too short"),
//        BADTYPE(401, "Token type Bearer"),
//        MARFORM(401, "Malformed Token"),
//        BADSIGN(401, "BadSignatured Token"),
//        EXPIRED(401, "Expired Token");
//
//        private int status;
//        private String msg;
//
//        TOKEN_ERROR(int status, String msg){
//            this.status = status;
//            this.msg = msg;
//        }
//
//        public int getStatus() {
//
//            return status;
//        }
//
//        public String getMsg(){
//
//            return msg;
//        }
//
//    }
//
//    public AccessTokenException(TOKEN_ERROR token_error){
//        super(token_error.name());
//        this.token_error = token_error;
//    }
//
//    public void sendResponseError(HttpServletResponse response){
//        response.setStatus(token_error.getStatus());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        Gson gson = new Gson();
//
//        String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(),"time", new Date()));
//
//        try{
//            response.getWriter().println(responseStr);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//}


import org.springframework.http.HttpStatus;

public class AccessTokenException extends RuntimeException {

    private final TOKEN_ERROR tokenError;

    @Getter
    public enum TOKEN_ERROR {
        UNACCEPT(HttpStatus.UNAUTHORIZED, "Token is null or too short", "토큰이 없거나 길이가 너무 짧습니다."),
        BADTYPE(HttpStatus.UNAUTHORIZED, "Token type Bearer", "토큰 타입은 Bearer여야 합니다."),
        MALFORM(HttpStatus.UNAUTHORIZED, "Malformed Token", "토큰 형식이 잘못되었습니다."),
        BADSIGN(HttpStatus.UNAUTHORIZED, "Badly Signed Token", "토큰 서명이 유효하지 않습니다."),
        EXPIRED(HttpStatus.UNAUTHORIZED, "Expired Token", "토큰이 만료되었습니다.");

        private final HttpStatus status;
        private final String developerMessage;
        private final String clientMessage;

        TOKEN_ERROR(HttpStatus status, String developerMessage, String clientMessage) {
            this.status = status;
            this.developerMessage = developerMessage;
            this.clientMessage = clientMessage;
        }

//        public HttpStatus getStatus() {
//            return status;
//        }
//
//        public String getDeveloperMessage() {
//            return developerMessage;
//        }
//
//        public String getClientMessage() {
//            return clientMessage;
//        }
    }

    public AccessTokenException(TOKEN_ERROR tokenError) {
        super(tokenError.getDeveloperMessage());
        this.tokenError = tokenError;
    }

    public HttpStatus getStatus() {
        return tokenError.getStatus();
    }

    public String getDeveloperMessage() {
        return tokenError.getDeveloperMessage();
    }

    public String getClientMessage() {
        return tokenError.getClientMessage();
    }

    public void sendResponseError(HttpServletResponse response){
        response.setStatus(tokenError.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("message", tokenError.getClientMessage(),"time", new Date()));

        try{
            response.getWriter().println(responseStr);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }
}



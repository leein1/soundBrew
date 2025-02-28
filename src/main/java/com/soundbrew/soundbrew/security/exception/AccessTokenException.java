package com.soundbrew.soundbrew.security.exception;


import com.google.gson.Gson;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException {

    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR{

        UNACCEPT(401, "Token is null or too short"),
        BADTYPE(401, "Token type Bearer"),
        MARFORM(401, "Malformed Token"),
        BADSIGN(401, "BadSignatured Token"),
        EXPIRED(401, "Expired Token");

        private int status;
        private String msg;

        TOKEN_ERROR(int status, String msg){
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {

            return status;
        }

        public String getMsg(){

            return msg;
        }

    }

    public AccessTokenException(TOKEN_ERROR token_error){
        super(token_error.name());
        this.token_error = token_error;
    }

    public void sendResponseError(HttpServletResponse response){
        response.setStatus(token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(),"time", new Date()));

        try{
            response.getWriter().println(responseStr);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}

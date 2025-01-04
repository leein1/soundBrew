package com.soundbrew.soundbrew.util;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    /*
    서명을 처리하기 위한 비밀키가 필요함
    application.properties에 추가했음

     */
    @Value("${com.soundbrew.jwt.secret}")
    private String key;

    //  문자열을 생성
    public String generateToken(Map<String, Object> valueMap, int days) {

        log.info("-------------키 생성 : " + key);

        //  헤더부분
        Map<String,Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //  payload 부분 설정
        Map<String,Object> payload = new HashMap<>();
        payload.putAll(valueMap);

        //  테스트를 위해 유효기간을 짧게 지정함
        int time = (60*24) * days; //   나중에 60*24 변경

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;

    }

    //  토큰을 검증
    public Map<String, Object> validateToken(String token) throws JwtException {

        Map<String,Object> claims = null;

        claims = Jwts.parser()
                .setSigningKey(key.getBytes()) //setKey
                .parseClaimsJws(token)  // 파싱 + 검증, 실패시 에러
                .getBody();


        return claims;
    }
}

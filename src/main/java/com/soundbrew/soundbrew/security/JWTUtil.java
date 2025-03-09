package com.soundbrew.soundbrew.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    @Value("${com.soundbrew.jwt.secret}")
    private String key;

    private byte[] getDecodedKey() {
        return Base64.getDecoder().decode(key);
    }

    //  문자열을 생성
    public String generateToken(Map<String, Object> valueMap, int days) {

        log.info("-------------JWTUTIL.generateToken 키 확인 : " + key);

        //  헤더부분
        Map<String,Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //  payload 부분 설정
        Map<String,Object> payload = new HashMap<>();
        payload.putAll(valueMap);

        int time = (1) * days; //   나중에 일 단위로 변경 해야 함

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
//                .setExpiration(Date.from(ZonedDateTime.now().plusDays(time).toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(time).toInstant()))
//                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .signWith(SignatureAlgorithm.HS256, getDecodedKey())
                .compact();

        return jwtStr;

    }

    //  문자열을 생성
    public String generateTokenWithMinutes(Map<String, Object> valueMap, int minutes) {

        log.info("-------------JWTUTIL.generateToken 키 확인 : " + key);

        //  헤더부분
        Map<String,Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //  payload 부분 설정
        Map<String,Object> payload = new HashMap<>();
        payload.putAll(valueMap);

        int time = (1) * minutes; //   나중에 일 단위로 변경 해야 함

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
//                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .signWith(SignatureAlgorithm.HS256, getDecodedKey())
                .compact();

        return jwtStr;

    }

    //  토큰을 검증
    public Map<String, Object> validateToken(String token) throws JwtException {

        log.info("JWTUtil.validateToken 토큰 검증 실행 : {}", token);

        Map<String,Object> claims = null;

        try {

            claims = Jwts.parser()
                    .setSigningKey(getDecodedKey()) //setKey
                    .parseClaimsJws(token)  // 파싱 + 검증, 실패시 에러
                    .getBody();

        }catch (Exception e){

            log.error("JWT 검증 실패: {}", e.getMessage());
            throw e;
        }

        return claims;
    }
}

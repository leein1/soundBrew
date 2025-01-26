//package com.soundbrew.soundbrew.util;
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@SpringBootTest
//@Log4j2
//public class JWTUtilTests {
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Test
//    public void testGenerateToken() {
//
//        Map<String,Object> claimMap = Map.of("username","ABCDE");
//
//        String jwtStr = jwtUtil.generateToken(claimMap,1);
//
//        log.info(jwtStr);
//    }
//
//    @Test
//    public void testValidToken(){
//
//        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzU3OTM1NjgsImlhdCI6MTczNTc5MzUwOCwidXNlcm5hbWUiOiJBQkNERSJ9.wn2Y9VjDL0Ol0MLR39AvzHiu2qE_hoqDa_T6i3YcVcM";
//
//        Map<String,Object> claim = jwtUtil.validateToken(jwtStr);
//
//        log.info(claim);
//    }
//
//    @Test
//    public void testALl(){
//
//        String jwtStr = jwtUtil.generateToken(Map.of("nickname","hello","email","test@test.com"),1);
//
//        log.info(jwtStr);
//
//        Map<String,Object> claim = jwtUtil.validateToken(jwtStr);
//
//        log.info("nickname : {}", claim.get("nickname"));
//        log.info("email : {}", claim.get("email"));
//    }
//}

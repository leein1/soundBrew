//package com.soundbrew.soundbrew.Service;
//
//import com.soundbrew.soundbrew.dto.RequestDTO;
//import com.soundbrew.soundbrew.dto.ResponseDTO;
//import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
//import com.soundbrew.soundbrew.service.AdminService;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Log4j2
//public class AdminServiceTests {
//
//    @Autowired
//    private AdminService adminService;
//
//    @Test
//    public void testFindAllUsers(){
//        RequestDTO requestDTO = new RequestDTO();
//
//        ResponseDTO<UserDetailsDTO> responseDTO = adminService.getAlluser(requestDTO);
//
//        log.info(responseDTO);
//
//    }
//}

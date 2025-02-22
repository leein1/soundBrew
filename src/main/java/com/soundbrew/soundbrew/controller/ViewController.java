package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import lombok.extern.log4j.Log4j2;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("")
public class ViewController {

    @GetMapping("/register")
    public void getRegister(){}

    @GetMapping("/activation")
    public void getActivation(){}

    @GetMapping("/login")
    public void getLogin(String error, String logout){
        log.info("login get-------------------------------");
        log.info("logout : {}", logout);

        if(logout != null){
            log.info("logout -----------------------------");
        }
    }

    @GetMapping("/help/find-password")
    public String findPassword(){

        return "findpw";
    }

    @GetMapping("/help/reset-password")
    public String resetPassword(){

        return "resetpw";
    }

    @GetMapping("/change-password")
    public String changePassword(){

        return "changepw";
    }

    @GetMapping("/mySubscription")
    public void getMySubscription(){}

    @GetMapping("/subscription")
    public void getSubscription(){}



    @GetMapping("/verificationPassword")
    public void getVerificationPassword(){}

    @GetMapping("/myInfo")
//    @PreAuthorize("hasRole('USER')")
    public void getMe(){

        log.warn("----------------------------------ViewController /MyInfo 호출");
    }

    @GetMapping("")
    public String getMain(){

        return "hello";

    }

    @GetMapping("/admin")
    public void adminMain(){}

    @GetMapping("/search")
    public String getSearch(RequestDTO requestDTO) {


        String link = requestDTO.getLink();

        return "redirect:/search" + link;

    }

    //====== 경동훈 SPA view ======

    @RequestMapping(value = { "/sounds/**" })
    public String soundSPA() {
        // 모든 SPA 요청을 sound/music-list.html로 리다이렉트
        return "sound/music-list";
    }

    @RequestMapping(value = {"/me/sounds/**"})
    public String soundManageSPA(){
        return "sound/music-manage";
    }

    //실제로는 /admin/sounds/...
    @RequestMapping(value= {"/admin/**"})
    public String soundAdmin(){
        return "admin/sounds";
    }
}

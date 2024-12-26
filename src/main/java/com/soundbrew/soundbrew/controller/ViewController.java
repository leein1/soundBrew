package com.soundbrew.soundbrew.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ViewController {

    @GetMapping("/register")
    public void getRegister(){}

    @GetMapping("/login")
    public void getLogin(){}

    @GetMapping("/verificationPassword")
    public void getVerificationPassword(){}

    @GetMapping("/myInfo")
    public void getMe(){}

    @GetMapping("")
    public String getMain(){
        return "/hello";
    }

    @GetMapping("/admin")
    public void adminMain(){}
}

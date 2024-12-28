package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    public void getMe(){}

    @GetMapping("")
    public String getMain(){
        return "/hello";
    }

    @GetMapping("/admin")
    public void adminMain(){}

    @GetMapping("/search")
    public String getSearch(RequestDTO requestDTO) {


        String link = requestDTO.getLink();

        return "redirect:/search" + link;

    }
}

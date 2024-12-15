package com.soundbrew.soundbrew.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {  //  관리자용 컨트롤러


    /*
        1. 관리자 자격 확인 과정 필요
        2. 관리자일 경우 필터링 각 메서드 마다 달리 적용 필요
     */




}

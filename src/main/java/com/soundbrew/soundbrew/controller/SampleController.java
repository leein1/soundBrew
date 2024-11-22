package com.soundbrew.soundbrew.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/stream")
@AllArgsConstructor
public class SampleController {
    // 테스트 페이지로 이동
    @GetMapping("/test-sound-page")
    public String soundPlayerTestPage() {
        return "soundTest";
    }
}

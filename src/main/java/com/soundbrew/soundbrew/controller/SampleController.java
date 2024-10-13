package com.soundbrew.soundbrew.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model){
        log.info("hello..");
        model.addAttribute("msg", "Hello World");
    }

    // 아래는 thymeleaf 와 view controller 테스트
    @GetMapping("/layout")
    public String getLayout(Model model){
        return "layouts/layout";
    }

    @GetMapping("/qna")
    public String getQna(Model model){
        return "board/qna";
    }

    @GetMapping("/album-list")
    public String getAlbumList(){
        return "sound/album-list";
    }

    @GetMapping("/artist-one")
    public String getArtistOne(){
        return "sound/artist-one";
    }

    @GetMapping("/music-list")
    public String getMusicList(){
        return "sound/music-list";
    }

    @GetMapping("/music-upload")
    public String getMusicUpload(){
        return "sound/music-upload";
    }

    @GetMapping("/sound")
    public String getSound(){
        return "/sound/sound";
    }
}

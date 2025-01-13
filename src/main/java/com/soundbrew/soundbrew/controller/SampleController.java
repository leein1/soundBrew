package com.soundbrew.soundbrew.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@AllArgsConstructor
public class SampleController {
    // 테스트 페이지로 이동
//    @GetMapping("/stream/test-sound-page")
//    public String soundPlayerTestPage() {
//        return "soundTest";
//    }
//
//    @GetMapping("/fetch/test-page")
//    public String fetchStandardTestPage(){
//        return  "fetchStandart";
//    }
//
//    @GetMapping("/form/test-page")
//    public String formTestPage(){
//        return "formTest";
//    }
//
//    @GetMapping("/render/musiclist")
//    public String renderTestPage(){
//        return "renderTest";
//    }
//
    @GetMapping("/sounds/tracks")
    public String getTracks(){
        return "/sound/music-list";
    }
//
//    @GetMapping("/sounds/albums")
//    public String getAlbums(){
//        return "/sound/album-list";
//    }
//
//    @GetMapping("/sounds/tracks/one")
//    public String getTracksOne(){
//        return "/sound/music-one";
//    }
//
//    @GetMapping("/sounds/albums/one")
//    public String getAlbumsOne(){
//        return "/sound/album-one";
//    }
//
//    @GetMapping("/manage/main")
//    public String getManageMain(){
//        return "/sound/manage/manage-main";
//    }
//    @GetMapping("/manage/albums")
//    public String getManageAlbums(){
//        return "/sound/manage/manage-albums";
//    }
//    @GetMapping("/manage/tracks")
//    public String getManageTracks(){
//        return "/sound/manage/manage-musics";
//    }
//    @GetMapping("/manage/tags")
//    public String getManageTags(){
//        return "/sound/manage/manage-tags";
//    }
//    @GetMapping("/new/sounds")
//    public String getUpload(){
//        return "/sound/music-upload";
//    }
//
//    @GetMapping("/soundplayer/test")
//    public String getPlayer() {
//        return "/player";
//    }
}

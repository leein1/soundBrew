package com.soundbrew.soundbrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SoundController {

    //1개의 앨범을 select, 해당 앨범의 곡 목록 나열.
    //parameter = album_id
    @GetMapping("/album-list")
    public String getAlbumList(Model model){
        return "sound/album-list";
    }

    // 1명의 아티스트를 select, 해당 가수의 곡 목록 나열
    // parameter = user_id(artist)
    @GetMapping("/artist-one")
    public String getArtistOne(Model model){
        return "sound/artist-one";
    }

    // sounds를 나열, sfx와 music의 구별 필요.
    // parameter = soundType(music, sfx), paging
    @GetMapping("/music-list")
    public String getMusicList(Model model){



        return "sound/music-list";
    }

    // sound를 업로드
    @GetMapping("/music-upload")
    public String getMusicUpload(Model model){
        return "sound/music-upload";
    }
}

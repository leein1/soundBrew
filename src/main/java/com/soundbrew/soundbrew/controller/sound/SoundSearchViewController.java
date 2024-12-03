package com.soundbrew.soundbrew.controller.sound;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SoundSearchViewController {
    @GetMapping("/sounds")
    public String getMusicList(){
        return "sound/music-list";
    }

    @GetMapping("/sounds/albums")
    public String getAlbumOne(){
        return "sound/album-one";
    }

    @GetMapping("/sounds/artists")
    public String getArtistOne(){
        return "sound/artist-one";
    }

    @GetMapping("/sounds/musics")
    public String getSoundOne(){
        return "sound/sound";
    }
}

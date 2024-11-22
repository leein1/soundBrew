package com.soundbrew.soundbrew.controller.sound;

import org.springframework.web.bind.annotation.GetMapping;

public class SoundSearchViewController {
    @GetMapping("/sounds")
    public String getMusicList(){
        return "sound/music-list";
    }

    @GetMapping("/sounds/albums/{albumId}")
    public String getAlbumOne(){
        return "sound/album-one";
    }

    @GetMapping("/sounds/artists/{nickname}")
    public String getArtistOne(){
        return "sound/artist-one";
    }

    @GetMapping("/sounds/musics/{musicId}")
    public String getSoundOne(){
        return "sound/sound";
    }
}

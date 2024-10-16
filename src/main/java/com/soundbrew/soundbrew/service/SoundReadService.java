package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.repository.AlbumMusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Service
public class SoundReadService {
    private AlbumMusicRepository albumMusicRepository;

    // music 검색
    // parameter : paging, soundType(music or sfx)
    // result : album, artist(user), music, tag
    public void soundSearch(){

    }

    // album 검색
    // parameter : album_id
    // result : album, artist, music, tag
    public void albumSearch(@RequestParam("album_id")int albumId, Model model){
//        model.addAttribute(albumMusicRepository.)
    }

    // artist 검색
    // parameter : user_id (artist)
    // result : album, artist, music, tag
    public void artistSearch(@RequestParam("user_id")int userId, Model model){

    }

    // song 검색
    // parameter : music_id
    // result : album, artist, music, tag
    public void songSearch(@RequestParam("music_id")int musicId, Model model){

    }

}

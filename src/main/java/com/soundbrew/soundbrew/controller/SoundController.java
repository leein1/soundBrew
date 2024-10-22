package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.SoundRequestDto;
import com.soundbrew.soundbrew.dto.SoundServiceDto;
import com.soundbrew.soundbrew.service.SoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SoundController {
    @Autowired
    private SoundService soundService;

    // sounds list
    // parameter : soundType(music, sfx), paging, *keyword, *tag
    @GetMapping("/sounds")
    public String getMusicList(@ModelAttribute SoundRequestDto soundRequestDto,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model){
        Pageable pageable = PageRequest.of(page, size);

        SoundServiceDto sounds = soundService.soundSearch(soundRequestDto, pageable);

        model.addAttribute("sounds", sounds.getSoundRepositoryDto());
        model.addAttribute("inst_tags", sounds.getInstTag());
        model.addAttribute("mood_tags", sounds.getMoodTag());
        model.addAttribute("genre_tags", sounds.getGenreTag());

        return "sound/music-list";
    }

    //1개의 앨범을 select, +해당 앨범의 곡 목록 나열.
    //parameter : album_id
    @GetMapping("/sound/album")
    public String getAlbumOne(@ModelAttribute SoundRequestDto soundRequestDto, Model model) {
        Pageable pageable = null;

        SoundServiceDto albums = soundService.soundSearch(soundRequestDto, pageable);
        model.addAttribute("albums", albums.getSoundRepositoryDto());
        return "sound/album-list";
    }

    // 1명의 아티스트를 select, +해당 가수의 곡 목록 나열
    // parameter : user_id(artist)
    @GetMapping("/sound/artist")
    public String getArtistOne(@ModelAttribute SoundRequestDto soundRequestDto, Model model){
        Pageable pageable= null;

        SoundServiceDto artist = soundService.soundSearch(soundRequestDto,pageable);
        model.addAttribute("artist", artist.getSoundRepositoryDto());
        return "sound/artist-one";
    }

    // 1개의 음악을 select
    // parameter : music_id
    @GetMapping("/sound")
    public String getSoundOne(@ModelAttribute SoundRequestDto soundRequestDto, Model model){
        Pageable pageable = null;

        SoundServiceDto sound = soundService.soundSearch(soundRequestDto,pageable);
        model.addAttribute("sound" , sound.getSoundRepositoryDto());
        model.addAttribute("inst_tags", sound.getInstTag());
        model.addAttribute("mood_tags", sound.getMoodTag());
        model.addAttribute("genre_tags", sound.getGenreTag());
        return "sound/sound";
    }


    // sound를 업로드
    @GetMapping("/sound/upload")
    public String getMusicUpload(@ModelAttribute SoundRequestDto soundRequestDto,
                                 Model model){
        return "sound/music-upload";
    }
}

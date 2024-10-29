package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchFilterDto;
import com.soundbrew.soundbrew.service.sound.SoundSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SoundReadController {
    @Autowired
    private SoundSearchService soundSearchService;

    // sounds list
    // parameter : soundType(music, sfx), paging, *keyword, *tag
    // dto : 음원들 리스트, 태그
    @GetMapping("/sounds")
    public String getMusicList(@ModelAttribute SoundSearchRequestDto soundSearchRequestDto,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model){
        Pageable pageable = PageRequest.of(page, size);

        SoundSearchFilterDto sounds = soundSearchService.soundSearch(soundSearchRequestDto, pageable);

        model.addAttribute("sounds", sounds.getSoundSearchResultDto());
        model.addAttribute("instTags", sounds.getInstTag());
        model.addAttribute("moodTags", sounds.getMoodTag());
        model.addAttribute("genreTags", sounds.getGenreTag());

        return "sound/music-list";
    }

    // 1. 1개의 앨범을 select, +해당 앨범의 곡 목록 나열. 2. 그 아티스트의 다른 앨범
    //parameter : album_id
    // "1개의 앨범을 select, +해당 앨범의 곡 목록 나열" - soundSearch , "그 아티스트의 다른 앨범" - 서치(아티스트 id)
    @GetMapping("/sound/album")
    public String getAlbumOne(@ModelAttribute SoundSearchRequestDto soundSearchRequestDto, Model model) {
        Pageable pageable = null;

        SoundSearchFilterDto albums = soundSearchService.soundSearch(soundSearchRequestDto, pageable);
        model.addAttribute("album", albums.getSoundSearchResultDto().get(0));
        model.addAttribute("sounds", albums.getSoundSearchResultDto());
        return "sound/album-list";
    }

    // 1. 1명의 아티스트를 select, +해당 가수의 곡 목록 나열 2. 그 아티스트의 앨범들.
    // parameter : user_id(artist)
    @GetMapping("/sound/artist")
    public String getArtistOne(@ModelAttribute SoundSearchRequestDto soundSearchRequestDto, Model model){
        Pageable pageable= null;

        SoundSearchFilterDto artist = soundSearchService.soundSearch(soundSearchRequestDto,pageable);
        model.addAttribute("artist", artist.getSoundSearchResultDto());
        return "sound/artist-one";
    }

    // 1개의 음악을 select
    // parameter : music_id
    @GetMapping("/sound")
    public String getSoundOne(@ModelAttribute SoundSearchRequestDto soundSearchRequestDto, Model model){
        Pageable pageable = null;

        SoundSearchFilterDto sound = soundSearchService.soundSearch(soundSearchRequestDto,pageable);
        model.addAttribute("sound" , sound.getSoundSearchResultDto());
        model.addAttribute("instTags", sound.getInstTag());
        model.addAttribute("moodTags", sound.getMoodTag());
        model.addAttribute("genreTags", sound.getGenreTag());
        return "sound/sound";
    }


    // sound를 업로드
    @GetMapping("/sound/upload")
    public String getMusicUpload(@ModelAttribute SoundSearchRequestDto soundSearchRequestDto,
                                 Model model){
        return "sound/music-upload";
    }
}

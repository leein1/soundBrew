package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.InstrumentTagDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.service.sound.SoundAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SoundAdminController {
    @Autowired
    private SoundAdminService soundAdminService;

    // 앨범 지우기
    @DeleteMapping("/admin/albums/{albumId}")
    public String deleteAlbum(@PathVariable("albumId") int albumId){
        // 권한 확인

        soundAdminService.deleteAlbum(albumId);

        return "empty";
    }

    // 음악 지우기
    @DeleteMapping("/admin/musics/{musicId}")
    public String deleteMusic(@PathVariable("musicId") int MusicId){
        // 권한 확인

        soundAdminService.deleteMusic(MusicId);

        return "empty";
    }

    // 인스트루먼트 태그 이름 바꾸기
    @PatchMapping("/admin/tags/instruments")
    public String changeInstSpelling(@ModelAttribute("beforeName") String beforeName, @ModelAttribute("afterName") String afterName){
        // 프로세서, 벨리데이터

        soundAdminService.updateInstrumentTagSpelling(beforeName,afterName);

        return "empty";
    }

    // 무드 태그 이름 바꾸기
    @PatchMapping("/admin/tags/moods")
    public String changeMoodSpelling(@ModelAttribute("beforeName") String beforeName, @ModelAttribute("afterName") String afterName){
        // 프로세서, 벨리데이터

        soundAdminService.updateGenreTagSpelling(beforeName,afterName);
        return "empty";
    }

    // 장르 태그 이름 바꾸기
    @PatchMapping("/admin/tags/genres")
    public String changeGenreSpelling(@ModelAttribute("beforeName") String beforeName, @ModelAttribute("afterName") String afterName){
        // 프로세서, 벨리데이터

        soundAdminService.updateGenreTagSpelling(beforeName,afterName);
        return "empty";
    }

    // 인스트루먼트 태그 만들기
    @PostMapping("/admin/tags/instruments")
    public String makeInstTag(@ModelAttribute InstrumentTagDto instrumentTagDto){
        // 프로세서, 벨리데이터

        soundAdminService.createInstTag(instrumentTagDto);
        return "empty";
    }

    // 무드 태그 이름 만들기
    @PostMapping("/admin/tags/moods")
    public String makeMoodTag(@ModelAttribute MoodTagDto moodTagDto){
        // 프로세서, 벨리데이터

        soundAdminService.createMoodTag(moodTagDto);
        return "empty";
    }

    // 장르 태그 이름 만들기
    @PostMapping("/admin/tags/genres")
    public String makeGenreTag(@ModelAttribute GenreTagDto genreTagDto){
        // 프로세서, 벨리데이터

        soundAdminService.createGenreTag(genreTagDto);
        return "empty";
    }
}

package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SoundManageController {
    @Autowired
    private SoundManagerService soundManagerService;

    //생성
    @PostMapping("/manage/sounds")
    public String createSound(@ModelAttribute AlbumDto albumDto,
                              @ModelAttribute MusicDto musicDto,
                              @ModelAttribute InstrumentTagDto instrumentTagDto,
                              @ModelAttribute MoodTagDto moodTagDto,
                              @ModelAttribute GenreTagDto genreTagDto){
        // 적절하게 프로세서, 벨리데이터

        // userid가져오기

        // 파일(이미지) 저장도 있어야함.
        soundManagerService.createSound(2,albumDto,musicDto,instrumentTagDto,moodTagDto,genreTagDto);

        return "파일..";
    }

    // 앨범 정보 업데이트 ( 제목, 설명)
    @PatchMapping("/manage/albums/{albumId}")
    public String updateAlbum(@ModelAttribute int albumId, @ModelAttribute AlbumDto albumDto){
        // 적절하게 프로세서, 벨리데이터

        // 앨범 이미지도 수정가능하다면, 이미지 관련 저장도 있어야함
        soundManagerService.updateAlbum(albumId,albumDto);

        return "";
    }

    // 음원 정보 업데이트 (타이틀, 설명, 음원타입)
    @PatchMapping("/manage/musics/{musicId}")
    public String updateMusic(@ModelAttribute int musicId, @ModelAttribute MusicDto musicDto){
        // 적절하게 프로세서, 벨리데이터

        soundManagerService.updateMusic(musicId,musicDto);

        return "";
    }

    // 태그 연결 업데이트
    @PostMapping("/manage/musics/{musicId}/tags")
    public String updateMusicTags(@ModelAttribute int musicId,
                                  @ModelAttribute InstrumentTagDto instrumentTagDto,
                                  @ModelAttribute MoodTagDto moodTagDto,
                                  @ModelAttribute GenreTagDto genreTagDto){

        soundManagerService.updateMusicTags(musicId,instrumentTagDto,moodTagDto,genreTagDto);

        return "";
    }


}

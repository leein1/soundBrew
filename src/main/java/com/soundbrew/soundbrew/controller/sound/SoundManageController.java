package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import com.soundbrew.soundbrew.service.sound.TagsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class SoundManageController {
    private final SoundServiceImpl soundService;
    private final TagsServiceImpl tagsService;
    //생성
    @PostMapping("/manage/sounds")
    public String createSound(@ModelAttribute("albumDto") AlbumDto albumDto,
                              @ModelAttribute("musicDto") MusicDto musicDto,
                              @ModelAttribute("tagsDto") TagsDto tagsDto){
        // 적절하게 프로세서, 벨리데이터

        // userid가져오기

        // 파일(이미지) 저장도 있어야함.
        soundService.createSound(2,albumDto,musicDto, tagsDto);
        return "empty";
    }

    // 앨범 정보 업데이트 ( 제목, 설명)
    @PatchMapping("/manage/albums/{albumId}")
    public String updateAlbum(@PathVariable("albumId") int albumId, @ModelAttribute("albumDto") AlbumDto albumDto){
        // 적절하게 프로세서, 벨리데이터

        // 앨범 이미지도 수정가능하다면, 이미지 관련 저장도 있어야함
        soundService.updateAlbum(albumId,albumDto);

        return "empty";
    }

    // 음원 정보 업데이트 (타이틀, 설명, 음원타입)
    @PatchMapping("/manage/musics/{musicId}")
    public String updateMusic(@PathVariable("musicId") int musicId, @ModelAttribute("musicDto") MusicDto musicDto){
        // 적절하게 프로세서, 벨리데이터

//        soundService.updateMusic(musicId,musicDto);

        return "empty";
    }

    // 태그 연결 업데이트
    @PostMapping("/manage/musics/{musicId}/tags")
    public String updateMusicTags(@PathVariable("musicId") int musicId,
                                  @ModelAttribute("tagsDto") TagsDto tagsDto){
        tagsService.updateSoundTags(musicId, tagsDto);

        return "empty";
    }
}

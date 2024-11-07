package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.service.sound.SoundSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class SoundManageViewController {
    @Autowired
    private SoundSearchService soundSearchService;

    // 관리페이지 호출(qna페이지 처럼 항목들로 주르륵)
    @GetMapping("/manage")
    public String getManagePage(){

        return "sound/manage/manage-sounds(미완성)";
    }

    // 앨범들 보기 (보면 get앨범 페이지로 이동하거나, 옆의 삭제 버튼 눌러서 삭제 기능 있음)
    @GetMapping("/manage/albums")
    public String getManageAlbumPage(Model model){
        // 앨범을 가져오는 서비스 메서드 호출(다른 정보는 필요없음) ** 본인꺼만.**

        // 개인정보를 가저오는 내부 메서드(ex. 쿠키 토큰 등,)
        String myName = "kimtest";
        model.addAttribute("myAlbums",soundSearchService.readAlbumByArtistName(myName));
        return "sound/manage/manage-albums(미완성)";
    }

    // 음악들 보기 (...)
    @GetMapping("/manage/musics")
    public String getManageMusicPage(Model model){
        // 음악을 가져오는 서비스 메서드 호출( 다른정보는 필요없) ** 본인꺼만.**

        // 개인정보를 가져오는 내부 메서드(ex. 쿠키 토큰 등)
        String myName = "kimtest";

        model.addAttribute("mySounds",soundSearchService.readMusicByArtistName(myName));
        return "sound/manage/manage-musics(미완성)";
    }

    // 음원 업로드 페이지
    @GetMapping("/manage/uploads")
    public String getMusicUpload(@ModelAttribute SoundSearchRequestDto soundSearchRequestDto,
                                 Model model){
        return "sound/manage/manage-uploads(미완성)";
    }
}

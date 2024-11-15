package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.service.sound.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SoundAdminViewController {
    private final TagsServiceImpl tagsService;
    private final SoundServiceImpl soundService;

    // 관리자 페이지 (qna 게시판 처럼 @@보기(..) 주르륵)
    @GetMapping("/admin")
    public String getAdminPage(){

        return "admin/sounds/admin-main";
    }

    // 태그들 보기 (이름변경, 태그 추가)
    @GetMapping("/admin/tags")
    public String getAdminTagPage(Model model){
        // 악기, 무드, 장르 태그들을 가져오는 서비스 메서드 호출
        model.addAttribute("tags",tagsService.readTags());


        return "admin/sounds/admin-tags";
    }

    // 앨범들 보기 (보면 get앨범 페이지로 이동하거나, 옆의 삭제 버튼 눌러서 삭제 기능 있음)
    @GetMapping("/admin/albums")
    public String getAdminAlbumPage(Model model){

        // 앨범을 가져오는 서비스 메서드 호출(다른 정보는 필요없음) ** 모든 정보
        model.addAttribute("albums", soundService.readAlbum());

        return "admin/sounds/admin-albums";
    }

    // 음악들 보기 (...)
    @GetMapping("/admin/musics")
    public String getAdminMusicPage(Model model){
        // 음악을 가져오는 서비스 메서드 호출( 다른정보는 필요없) ** 모든 정보
        model.addAttribute("sounds", soundService.readMusic());

        return "admin/sounds/admin-musics";
    }
}

package com.soundbrew.soundbrew.controller.sound;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SoundManageViewController {

    @GetMapping("/manage")
    public String getManagePage() {
        return "sound/manage/manage-main";
    }

    @GetMapping("/manage/albums")
    public String getManageAlbumPage() {
        return "sound/manage/manage-albums";
    }

    @GetMapping("/manage/musics")
    public String getManageMusicPage() {
        return "sound/manage/manage-musics";
    }

    @GetMapping("/manage/uploads")
    public String getMusicUpload() {
        return "sound/manage/manage-uploads";
    }

    @GetMapping("/manage/tags")
    public String getMyTagChange() {
        return "sound/manage/manage-tags";
    }

}

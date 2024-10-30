package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundManagerService;
import com.soundbrew.soundbrew.service.util.FilteringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.soundbrew.soundbrew.service.util.FilteringUtil.*;

@Controller
public class SoundManageController {
    @Autowired
    private SoundManagerService soundManagerService;

    @PostMapping()
    public String createSound(@ModelAttribute AlbumDto albumDto,
                              @ModelAttribute MusicDto musicDto,
                              @ModelAttribute InstrumentTagDto instrumentTagDto,
                              @ModelAttribute MoodTagDto moodTagDto,
                              @ModelAttribute GenreTagDto genreTagDto){
        // 적절하게 트림 (...)
        albumDto.setAlbumName(removeEndpointsWhiteSpace(albumDto.getAlbumName()));
//        albumDto.setDescription(removeEndpointsWhiteSpace(albumDto.getDescription()));
//        musicDto.setDescription(removeEndpointsWhiteSpace(musicDto.getDescription()));

        if (!hasNoSpecialCharacters(albumDto.getAlbumName())) {
            throw new IllegalArgumentException("앨범 이름에 특수 문자 안됨");
            return "/error";
        }
        if(!isLengthWithinBounds(albumDto.getAlbumName(),2,50)){
            throw new IllegalArgumentException("글자수 ㅈㄴ 김");
            return  "/error";
        }


        albumfilterService.checkvalid(albumDto);

        return "파일..";
    }
}

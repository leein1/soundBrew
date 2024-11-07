package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchFilterDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchResultDto;
import com.soundbrew.soundbrew.service.sound.SoundSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


// #1. request dto 에서 요청에 해당하는 파라미터가 매핑이 되고, 값이 있는지 확인을 해보는 것.
// #2. 서비스에서 나온 결과들을 적절하게 model에 태우는 것.
@Controller
public class SoundSearchController {
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
    @GetMapping("/sounds/albums/{albumId}")
    public String getAlbumOne(@PathVariable("albumId") int albumId, Model model) {
        // 파라미터 사전 준비
        SoundSearchRequestDto soundSearchRequestDto = new SoundSearchRequestDto();
        soundSearchRequestDto.setAlbumId(albumId);
        Pageable pageable = null; // 페이징 필요없음

        // 1. 받은 albumid로 검색
        SoundSearchFilterDto albums = soundSearchService.soundSearch(soundSearchRequestDto, pageable);
        // 2. 1개를 담은 album 만들어두기 (album)
        SoundSearchResultDto album = albums.getSoundSearchResultDto().isEmpty() ? null : albums.getSoundSearchResultDto().get(0);
        // 3. 그리고 album에 있는 username(artist name)을 토대로 아티스트의 다른 앨범 목록도 준비해서 넘기기 (otherAlbums)
        List<AlbumDto> albumDto = soundSearchService.readAlbumByArtistName(album.getUserName());

        // album list
        model.addAttribute("otherAlbums", albumDto);
        //album one
        model.addAttribute("album", album);
        // music list
        model.addAttribute("sounds", albums.getSoundSearchResultDto());

        //album, albums,

        return "sound/album-one(미완성)";
    }

    // 1. 1명의 아티스트를 select, +해당 가수의 곡 목록 나열 2. 그 아티스트의 앨범들.
    // parameter : user_id(artist)
    @GetMapping("/sounds/artists/{nickname}")
    public String getArtistOne(@PathVariable("nickname") String nickname, Model model){
        // 파라미터 사전 준비
        SoundSearchRequestDto soundSearchRequestDto = new SoundSearchRequestDto();
        soundSearchRequestDto.setNickname(nickname);
        Pageable pageable= null; // 페이징 필요없 ( 1개 선택)

        SoundSearchFilterDto artist = soundSearchService.soundSearch(soundSearchRequestDto,pageable);
        model.addAttribute("artist", artist.getSoundSearchResultDto());
        return "artist-one(미완성)";
    }

    // 1개의 음악을 select
    // parameter : music_id
    @GetMapping("/sounds/musics/{musicId}")
    public String getSoundOne(@PathVariable("musicId")int musicId, Model model){
        // 파라미터 사전 준비
        SoundSearchRequestDto soundSearchRequestDto = new SoundSearchRequestDto();
        soundSearchRequestDto.setMusicId(musicId);
        Pageable pageable = null; // 페이징 필요없 ( 1개 선택)

        SoundSearchFilterDto sound = soundSearchService.soundSearch(soundSearchRequestDto,pageable);
        model.addAttribute("sound" , sound.getSoundSearchResultDto());
        model.addAttribute("instTags", sound.getInstTag());
        model.addAttribute("moodTags", sound.getMoodTag());
        model.addAttribute("genreTags", sound.getGenreTag());
        return "sound(미완성)";
    }

}

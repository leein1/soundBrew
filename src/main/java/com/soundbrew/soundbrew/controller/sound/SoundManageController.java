package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import com.soundbrew.soundbrew.service.sound.TagsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SoundManageController {
    private final SoundServiceImpl soundService;
    private final TagsServiceImpl tagsService;

    // 앨범 가져오기(나의) read
    // 1. 나의 권한을 서비스 로직에 같이 보낸다, 2. 서비스로직에서 권한에 따라 메서드를 분기한다.
    @GetMapping("/albums/mine")
    public ResponseEntity<ResponseDto<AlbumDto>> getMyAlbums(RequestDto requestDto) {
        // RequestDto 객체 생성(실제로는 프론트에서 이미 값을 추출해서 넘겨주기때문에 나중에는 지우기)
        RequestDto test = RequestDto.builder().type("n").keyword("u_1").build();

        ResponseDto<AlbumDto> responseDto = soundService.getUsersAlbums(requestDto);
        //ResponseDto<AlbumDto> responseDto = soundService.searchAlbum(requestDto,권한);
        if(responseDto.getDto().isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(responseDto);
    }

    // 음악 가져오기(나의) read
    // 1. 나의 권한을 서비스 로직에 같이 보낸다, 2. 서비스로직에서 권한에 따라 메서드를 분기한다.
    @GetMapping("/sounds/mine")
    public ResponseEntity<ResponseDto<MusicDto>> getMySounds(RequestDto requestDto) {
        // RequestDto 객체 생성(실제로는 프론트에서 이미 값을 추출해서 넘겨주기때문에 나중에는 지우기)
        RequestDto test = RequestDto.builder().type("n").keyword("u_1").build();

        ResponseDto<MusicDto> responseDto = soundService.getUsersSounds(requestDto);
        //ResponseDto<MusicDto> responseDto = soundService.searchMusic(requestDto,권한);
        if(responseDto.getDto().isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(responseDto);
    }

    // 태그들 가져오기(나의) ( 아티스트의 곡을 중심으로 태그를 긁어옴)
    @GetMapping("/tags/mine")
    public ResponseEntity<ResponseDto<TagsDto>> getTagsForMyMusics(RequestDto requestDto) {
        // RequestDto 객체 생성(실제로는 프론트에서 이미 값을 추출해서 넘겨주기때문에 나중에는 지우기)
        RequestDto test = RequestDto.builder().type("n").keyword("u_1").build();

        // if(reqeust.getKeyword()와 JWT.get..이 같은지를 통해서 내 명의의 정보를 확인할려는 건지 확인){
        ResponseDto<MusicDto> musics = soundService.getUsersSounds(requestDto);
        if(musics.getDto().isEmpty()) return ResponseEntity.noContent().build();

        List<Integer> musicIds = musics.getDto().stream().map(MusicDto :: getMusicId).collect(Collectors.toList());
        ResponseDto<TagsDto> tagsList = tagsService.readTagsByMusicIds(musicIds);

        return ResponseEntity.ok().body(tagsList);
        //} else{
        // return ResponseEntity.noContent().build();
        //}
    }

    //생성
    @PostMapping("/manage/sounds")
    public ResponseEntity<Void> createSounds(@RequestBody SoundCreateDto soundCreateDto){
        // 적절하게 프로세서, 벨리데이터
        AlbumDto albumDto = soundCreateDto.getAlbumDto();
        MusicDto musicDto = soundCreateDto.getMusicDto();
        TagsDto tagsDto = soundCreateDto.getTagsDto();
        // userid가져오기

        // 파일(이미지) 저장도 있어야함.
        soundService.createSound(2,albumDto,musicDto, tagsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 앨범 정보 업데이트 (제목, 설명)
    @PatchMapping("/manage/albums/{albumId}")
    public ResponseEntity<Void> updateAlbumInfo(@PathVariable int albumId, @RequestBody AlbumDto albumDto) {
        //벨리데이트 프로세서
        soundService.updateAlbum(albumId,albumDto);
        return ResponseEntity.ok().build();
    }

    // 음원 정보 업데이트 (타이틀, 설명, 음원 타입)
    @PatchMapping("/manage/sounds/{musicId}")
    public ResponseEntity<Void> updateMusicInfo(@PathVariable int musicId, @RequestBody MusicDto musicDto) {
        //벨리데이트 프로세서
        soundService.updateMusic(musicId, musicDto);
        return ResponseEntity.ok().build();
    }

    // 태그 연결 업데이트
    @PostMapping("/manage/sounds/{musicId}/tags")
    public ResponseEntity<Void> updateMusicTags(@PathVariable int musicId, @RequestBody TagsDto tagsDto) {
        //벨리데이트 프로세서
        tagsService.updateSoundTags(musicId, tagsDto);
        return ResponseEntity.ok().build();
    }

}

package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import com.soundbrew.soundbrew.service.sound.TagsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SoundManageController {
    private final SoundServiceImpl soundService;
    private final TagsServiceImpl tagsService;
    //생성
    @PostMapping("/manage/sounds")
    public ResponseEntity<Void> createSound(@ModelAttribute("albumDto") AlbumDto albumDto,
                                              @ModelAttribute("musicDto") MusicDto musicDto,
                                              @ModelAttribute("tagsDto") TagsDto tagsDto){
        // 적절하게 프로세서, 벨리데이터

        // userid가져오기

        // 파일(이미지) 저장도 있어야함.
        soundService.createSound(2,albumDto,musicDto, tagsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 앨범 정보 업데이트 (제목, 설명)
    @PatchMapping("/manage/albums/{albumId}")
    public ResponseEntity<Void> updateAlbum(@PathVariable int albumId, @RequestBody AlbumDto albumDto) {
        //벨리데이트 프로세서
        return ResponseEntity.ok().build();
    }

    // 음원 정보 업데이트 (타이틀, 설명, 음원 타입)
    @PatchMapping("/manage/musics/{musicId}")
    public ResponseEntity<Void> updateMusic(@PathVariable int musicId, @RequestBody MusicDto musicDto) {
        //벨리데이트 프로세서
        soundService.updateMusic(musicId, musicDto);
        return ResponseEntity.ok().build();
    }

    // 태그 연결 업데이트
    @PostMapping("/manage/musics/{musicId}/tags")
    public ResponseEntity<Void> updateMusicTags(@PathVariable int musicId, @RequestBody TagsDto tagsDto) {
        //벨리데이트 프로세서
        tagsService.updateSoundTags(musicId, tagsDto);
        return ResponseEntity.ok().build();
    }

    // 앨범 가져오기(나의)
    @GetMapping("/manage/albums")
    public ResponseEntity<ResponseDto<AlbumDto>> getMyAlbums(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        // 토큰같은거에서 Role이 artist고, userName정보가 있다면 아티스트 대상 검색
        String myName = "u_1"; // 사용자 이름 가져오는 로직은 추후 추가
        Pageable pageable = PageRequest.of(page, size);

        Optional<List<AlbumDto>> albumDtos = soundService.readAlbumByArtistName(myName);
        if(albumDtos.isEmpty()) return ResponseEntity.noContent().build(); //http204

        ResponseDto<AlbumDto> response = ResponseDto.<AlbumDto>builder().dto(albumDtos.get()).pageable(pageable).build();

        return ResponseEntity.ok().body(response);
    }

    // 음악 가져오기(나의)
    @GetMapping("/manage/musics")
    public ResponseEntity<ResponseDto<MusicDto>> getMyMusics(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        // 토큰같은거에서 Role이 artist고, userName정보가 있다면 아티스트 대상 검색
        String myName = "u_1"; // 사용자 이름 가져오는 로직은 추후 추가
        Pageable pageable =PageRequest.of(page,size);

        Optional<List<MusicDto>> musicDtos = soundService.readMusicByArtistName(myName);

        if(musicDtos.isEmpty()) return ResponseEntity.noContent().build(); //http204

        ResponseDto<MusicDto> response = ResponseDto.<MusicDto>builder().dto(musicDtos.get()).pageable(pageable).build();
        return ResponseEntity.ok().body(response);
    }

    // 태그 정보 가져오기 (추가 구현 필요)
    @GetMapping("/manage/tags")
    public ResponseEntity<List<String>> getMyTags() {
        String myName = "u_1"; // 사용자 이름 가져오는 로직은 추후 추가
        Optional<List<MusicDto>> musicDtos = soundService.readMusicByArtistName(myName);
        if(musicDtos.isEmpty()) return ResponseEntity.noContent().build();

        // musicDtos를 Optional에서 꺼내고, 각 MusicDto에서 musicId를 추출
        List<Integer> musicIds = musicDtos.get().stream()
                .map(MusicDto::getMusicId) // MusicDto에서 musicId를 가져옴
                .collect(Collectors.toList());

        tagsService.readTagsByMusicId(musicIds);
        return ResponseEntity.ok().body(null);
    }

}

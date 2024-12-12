package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import com.soundbrew.soundbrew.service.sound.TagsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SoundAdminController {
    private final SoundServiceImpl soundService;
    private final TagsServiceImpl tagsService;

    // 음원 검색( only sound)
    // 1. 나의 권한을 서비스 로직에 같이 보낸다 2. 서비스 로직에서 권한에 따라 메서드를 분기한다.
    @GetMapping("/admin/sounds")
    public ResponseEntity<ResponseDto<SearchTotalResultDto>> getUsersSounds(RequestDto requestDto) {
        ResponseDto<SearchTotalResultDto> responseDto = soundService.getUsersSounds(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    // 앨범 검색( only album)
    // 1. 나의 권한을 서비스 로직에 같이 보낸다 2. 서비스 로직에서 권한에 따라 메서드를 분기한다.
    @GetMapping("/admin/albums")
    public ResponseEntity<ResponseDto<SearchTotalResultDto>> getUsersAlbums(RequestDto requestDto) {
        ResponseDto<SearchTotalResultDto> responseDto = soundService.getUsersAlbums(requestDto);
//        ResponseDto<AlbumDto> responseDto = soundService.searchAlbum(requestDto,권한);

        return ResponseEntity.ok().body(responseDto);
    }

    // 태그들 불러오기
    @GetMapping("/admin/tags")
    public ResponseEntity<ResponseDto<TagsDto>> getAllTags() {
        ResponseDto<TagsDto> tagsDto = tagsService.readTags();
        if (tagsDto.getDtoList().isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(tagsDto);
    }

    // 앨범 지우기
    @DeleteMapping("/admin/albums/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable("albumId") int albumId) {
        soundService.deleteAlbum(albumId);
        return ResponseEntity.ok().build();
    }

    // 음악 지우기
    @DeleteMapping("/admin/musics/{musicId}")
    public ResponseEntity<Void> deleteMusic(@PathVariable("musicId") int musicId) {
        soundService.deleteMusic(musicId);
        return ResponseEntity.ok().build();
    }

    // 인스트루먼트 태그 이름 바꾸기
    @PatchMapping("/admin/tags/{}/instruments/{tagName}")
    public ResponseEntity<Void> changeInstSpelling(@PathVariable("tagName") String beforeName,
                                                   @RequestParam("afterName") String afterName) {
        tagsService.updateInstrumentTagSpelling(beforeName, null);
        return ResponseEntity.ok().build();
    }

    // 무드 태그 이름 바꾸기
    @PatchMapping("/admin/tags/moods/{tagName}")
    public ResponseEntity<Void> changeMoodSpelling(@PathVariable("tagName") String beforeName,
                                                   @RequestParam("afterName") String afterName) {
        tagsService.updateMoodTagSpelling(beforeName, afterName);
        return ResponseEntity.ok().build();
    }

    // 장르 태그 이름 바꾸기
    @PatchMapping("/admin/tags/genres/{tagName}")
    public ResponseEntity<Void> changeGenreSpelling(@PathVariable("tagName") String beforeName,
                                                    @RequestParam("afterName") String afterName) {
        tagsService.updateGenreTagSpelling(beforeName, afterName);
        return ResponseEntity.ok().build();
    }

    // 인스트루먼트 태그 만들기
    @PostMapping("/admin/tags/instruments")
    public ResponseEntity<Void> makeInstTag(@RequestBody TagsDto tagsDto) {
        tagsService.createInstTag(tagsDto);
        return ResponseEntity.ok().build();
    }

    // 무드 태그 만들기
    @PostMapping("/admin/tags/moods")
    public ResponseEntity<Void> makeMoodTag(@RequestBody TagsDto tagsDto) {
        tagsService.createMoodTag(tagsDto);
        return ResponseEntity.ok().build();
    }

    // 장르 태그 만들기
    @PostMapping("/admin/tags/genres")
    public ResponseEntity<Void> makeGenreTag(@RequestBody TagsDto tagsDto) {
        tagsService.createGenreTag(tagsDto);
        return ResponseEntity.ok().build();
    }

}

package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import com.soundbrew.soundbrew.service.sound.TagsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SoundAdminController {
    private final SoundServiceImpl soundService;
    private final TagsServiceImpl tagsService;

    @GetMapping("/admin/musics")
    public ResponseEntity<ResponseDto<MusicDto>> readMusics(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);

        Optional<List<MusicDto>> musics = soundService.readMusic();
        if(musics.isEmpty()) return ResponseEntity.noContent().build();

        ResponseDto<MusicDto> response = ResponseDto.<MusicDto>builder().dto(musics.get()).pageable(pageable).build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/albums")
    public ResponseEntity<ResponseDto<AlbumDto>> readAlbums(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);

        Optional<List<AlbumDto>> albums = soundService.readAlbum();
        if(albums.isEmpty()) return ResponseEntity.noContent().build();

        ResponseDto<AlbumDto> responseDto = ResponseDto.<AlbumDto>builder().dto(albums.get()).pageable(pageable).build();
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/admin/albums/{artist}")
    public ResponseEntity<ResponseDto<AlbumDto>> readAlbumsByArtistName(@PathVariable String artist, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        // 토큰같은거에서 Role이 admin이고, 키워드가 있어야함.
         artist = "u_1"; // 사용자 이름 가져오는 로직은 추후 추가

        Optional<List<AlbumDto>> albumDtos = soundService.readAlbumByArtistName(artist);
        if(albumDtos.isEmpty()) return ResponseEntity.noContent().build(); //http204

        ResponseDto<AlbumDto> responseDto = ResponseDto.<AlbumDto>builder().dto(albumDtos.get()).pageable(pageable).build();

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/admin/sounds/{artist}")
    public ResponseEntity<ResponseDto<MusicDto>> readSoundsByArtistName(@PathVariable String artist, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        // 토큰같은거에서 Role이 admin이고, 키워드가 있어야함.
         artist = "u_1"; // 사용자 이름 가져오는 로직은 추후 추가

        Optional<List<MusicDto>> musicDtos = soundService.readMusicByArtistName(artist);
        if(musicDtos.isEmpty()) return ResponseEntity.noContent().build(); //http204

        ResponseDto<MusicDto> responseDto = ResponseDto.<MusicDto>builder().dto(musicDtos.get()).pageable(pageable).build();

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/admin/tags")
    public ResponseEntity<TagsDto> getAdminTagPage(){
        Optional<TagsDto> tagsDto = tagsService.readTags();
        if(tagsDto.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(tagsDto.get());
    }

    //============================================================================================================

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
    @PatchMapping("/admin/tags/instruments")
    public ResponseEntity<String> changeInstSpelling(@RequestParam("beforeName") String beforeName,
                                                   @RequestParam("afterName") String afterName) {
        tagsService.updateInstrumentTagSpelling(beforeName, afterName);
        return ResponseEntity.ok().body(afterName);
    }

    // 무드 태그 이름 바꾸기
    @PatchMapping("/admin/tags/moods")
    public ResponseEntity<String> changeMoodSpelling(@RequestParam("beforeName") String beforeName,
                                                   @RequestParam("afterName") String afterName) {
        tagsService.updateMoodTagSpelling(beforeName, afterName);
        return ResponseEntity.ok().body(afterName);
    }

    // 장르 태그 이름 바꾸기
    @PatchMapping("/admin/tags/genres")
    public ResponseEntity<String> changeGenreSpelling(@RequestParam("beforeName") String beforeName,
                                                    @RequestParam("afterName") String afterName) {
        tagsService.updateGenreTagSpelling(beforeName, afterName);
        return ResponseEntity.ok().body(afterName);
    }

    // 인스트루먼트 태그 만들기
    @PostMapping("/admin/tags/instruments")
    public ResponseEntity<TagsDto> makeInstTag(@RequestBody TagsDto tagsDto) {
        tagsService.createInstTag(tagsDto);
        return ResponseEntity.ok().body(tagsDto);
    }

    // 무드 태그 만들기
    @PostMapping("/admin/tags/moods")
    public ResponseEntity<TagsDto> makeMoodTag(@RequestBody TagsDto tagsDto) {
        tagsService.createMoodTag(tagsDto);
        return ResponseEntity.ok().body(tagsDto);
    }

    // 장르 태그 만들기
    @PostMapping("/admin/tags/genres")
    public ResponseEntity<TagsDto> makeGenreTag(@RequestBody TagsDto tagsDto) {
        tagsService.createGenreTag(tagsDto);
        return ResponseEntity.ok().body(tagsDto);
    }
}

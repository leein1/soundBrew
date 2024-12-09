package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SoundSearchController {
    private final SoundServiceImpl soundService;

    // **민감정보는 숨기기
    // otherAlbums
    @GetMapping("/albums/other")//title, nickname
    public ResponseEntity<ResponseDto<AlbumDto>> getOtherAlbums(RequestDto requestDto) {
        // RequestDto 객체 생성 (실제로는 페이지가 로딩되면서 클라이언트측에서 uri를 완성하고 자동으로 요청을 보냄 즉, 후에 지워도 됨)
        RequestDto test = RequestDto.builder().type("n").keyword("u_1").build();

        // 서비스 호출
        ResponseDto<AlbumDto> responseDto = soundService.getUsersAlbums(requestDto);
        if(responseDto.getDto().isEmpty()) return ResponseEntity.noContent().build();

        // ResponseEntity로 ResponseDto 반환
        return ResponseEntity.ok(responseDto);
    }

    // **민감정보는 숨기기
    // total Albums
    @GetMapping("/albums")
    public ResponseEntity<ResponseDto<SearchAlbumResultDto>> searchAlbums(RequestDto searchRequestDto) {
        ResponseDto<SearchAlbumResultDto> albums = soundService.totalAlbumSearch(searchRequestDto);
        if (albums.getDto().isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(albums);
    }

    // **민감정보는 숨기기
    // total sounds
    @GetMapping("/sounds")
    public ResponseEntity<ResponseDto<SearchResponseDto>> searchMusics(RequestDto requestDto){
        ResponseDto<SearchResponseDto> sounds = soundService.totalSoundSearch(requestDto);
        if(sounds.getDto().isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(sounds);
    }
}

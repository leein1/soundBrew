package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.service.sound.SoundServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SoundSearchController {
    private final SoundServiceImpl soundService;

    @GetMapping("/sounds")
    public ResponseEntity<SearchResponseDto> getMusicList(@ModelAttribute SearchRequestDto searchRequestDto,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                          Model model){
        Pageable pageable = PageRequest.of(page, size);

        Optional<SearchResponseDto> sounds = soundService.soundSearch(searchRequestDto, pageable);
        if(sounds.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(sounds.get());
    }

    @GetMapping("/sounds/albums/{albumId}")
    public ResponseEntity<SearchResponseDto> getAlbumOne(@PathVariable("albumId") int albumId, Model model) {
        SearchRequestDto request = SearchRequestDto.builder().albumId(albumId).build();

        Optional<SearchResponseDto> albums = soundService.soundSearch(request, null);
        if(albums.isEmpty()) return ResponseEntity.noContent().build();

        SearchResultDto album = albums.get().getSearchResultDto().isEmpty() ? null : albums.get().getSearchResultDto().get(0);
        Optional<List<AlbumDto>> albumDto = soundService.readAlbumByArtistName(album.getUserName());
        albums.get().setOtherAlbums(albumDto.get());

        return ResponseEntity.ok().body(albums.get());
    }

    @GetMapping("/sounds/artists/{nickname}")
    public ResponseEntity<SearchResponseDto> getArtistOne(@PathVariable("nickname") String nickname, Model model){
        SearchRequestDto searchRequestDto = SearchRequestDto.builder().nickname(nickname).build();

        Optional<SearchResponseDto> artist = soundService.soundSearch(searchRequestDto,null);
        if(artist.isEmpty()) return ResponseEntity.noContent().build();

        SearchResultDto album = artist.get().getSearchResultDto().isEmpty() ? null : artist.get().getSearchResultDto().get(0);
        Optional<List<AlbumDto>> albumDto = soundService.readAlbumByArtistName(album.getUserName());
        artist.get().setOtherAlbums(albumDto.get());

        return ResponseEntity.ok().body(artist.get());
    }

    @GetMapping("/sounds/musics/{musicId}")
    public ResponseEntity<SearchResponseDto> getSoundOne(@PathVariable("musicId")int musicId, Model model){
        SearchRequestDto searchRequestDto = SearchRequestDto.builder().musicId(musicId).build();

        Optional<SearchResponseDto> sound = soundService.soundSearch(searchRequestDto,null);

        return ResponseEntity.ok().body(sound.get());
    }
}

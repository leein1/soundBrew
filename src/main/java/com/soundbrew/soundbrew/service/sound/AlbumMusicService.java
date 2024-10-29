package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.domain.sound.AlbumMusicId;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.SoundSearchResultDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.repository.sound.AlbumMusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumMusicService {
    @Autowired
    private AlbumMusicRepository albumMusicRepository;

    public void createAlbumMusic(Album album, Music music, User checkedUser){
        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(new AlbumMusicId(album.getAlbumId(),music.getMusicId(),music.getUserId()))
                .album(album)
                .music(music)
                .user(checkedUser)
                .build();
        albumMusicRepository.save(albumMusic);
    }

    public List<SoundSearchResultDto> readTatal(SoundSearchRequestDto soundSearchRequestDto, Pageable pageable){
        List<SoundSearchResultDto> before = albumMusicRepository.search(soundSearchRequestDto, pageable);
        return before;
    }

}

package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.repository.AlbumMusicRepository;
import com.soundbrew.soundbrew.repository.AlbumRepository;
import com.soundbrew.soundbrew.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SoundRead {
    private AlbumMusicRepository musicRepository;

    // music 검색
    // parameter : paging, soundType(music)
    // result : album, artist(user), music, tag

    // album 검색
    // parameter : album_id
    // result : album, artist, music, tag

    // artist 검색
    // parameter : user_id (artist)
    // result : album, artist, music, tag

    // song 검색
    // parameter : music_id
    // result : album, artist, music, tag
}

package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SoundManagerService {
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumService albumService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private AlbumMusicService albumMusicService;

    @Autowired
    private InstrumentTagService instrumentTagService;
    @Autowired
    private MoodTagService moodTagService;
    @Autowired
    private GenreTagService genreTagService;

    @Transactional
    public void createSound(int checkedUserId,AlbumDto albumDto, MusicDto musicDto, InstrumentTagDto instrumentTagDto, MoodTagDto moodTagDto, GenreTagDto genreTagDto ){
        //추가적인 사전작업
        User ckeckedUser = userRepository.findById(2).orElseThrow();
        albumDto.setUserId(checkedUserId);
        musicDto.setUserId(checkedUserId);

        Album album = albumService.createAlbum(albumDto);
        Music music = musicService.createMusic(musicDto);
        albumMusicService.createAlbumMusic(album,music,ckeckedUser);
        instrumentTagService.plusMusicInstrumentTag(music,instrumentTagDto);
        moodTagService.plusMusicMoodTag(music,moodTagDto);
        genreTagService.plusMusicGenreTag(music, genreTagDto);
    }

    @Transactional
    public void updateAlbum(int albumId, AlbumDto albumDto){
        albumService.updateAlbum(albumId,albumDto);
    }

    @Transactional
    public void updateMusic(int musicId, MusicDto musicDto ){
        // 인증 기능
        musicService.updateMusic(musicId,musicDto);
    }

    @Transactional
    public void updateMusicTags(int musicId, InstrumentTagDto instrumentTagDto, MoodTagDto moodTagDto, GenreTagDto genreTagDto) {
        // 인증 기능
        Music music = musicRepository.findById(musicId).orElseThrow();
        if (instrumentTagDto != null) {instrumentTagService.updateMusicInstrumentTag(music, instrumentTagDto);}
        if (moodTagDto != null) {moodTagService.updateMusicMoodTag(music, moodTagDto);}
        if (genreTagDto != null) {genreTagService.updateMusicGenreTag(music, genreTagDto);}
    }
}
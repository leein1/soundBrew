package com.soundbrew.soundbrew.service.sound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SoundAdminService {
    @Autowired
    private MusicService musicService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private TagsService tagsService;

    @Transactional
    public void deleteAlbum(int albumId){
        // 인증 기능
        albumService.deleteAlbum(albumId);
    }

    @Transactional
    public void deleteMusic(int musicId){
        // 인증 기능
       musicService.deleteMusic(musicId);
    }

    @Transactional
    public void updateInstrumentTagSpelling(String beforeName, String afterName){
        // 인증
        tagsService.updateInstrumentTagSpelling(beforeName,afterName);
    }

    @Transactional
    public void updateMoodTagSpelling(String beforeName, String afterName){
        // 인증
        tagsService.updateMoodTagSpelling(beforeName,afterName);
    }

    @Transactional
    public void updateGenreTagSpelling(String beforeName, String afterName){
        // 인증
        tagsService.updateGenreTagSpelling(beforeName,afterName);
    }
}

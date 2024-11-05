package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.InstrumentTagDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.repository.sound.MoodTagRepository;
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
    private InstrumentTagService instrumentTagService;
    @Autowired
    private MoodTagService moodTagService;
    @Autowired
    private GenreTagService genreTagService;

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
        instrumentTagService.updateInstrumentTagSpelling(beforeName,afterName);
    }

    @Transactional
    public void updateMoodTagSpelling(String beforeName, String afterName){
        // 인증
        moodTagService.updateMoodTagSpelling(beforeName,afterName);
    }

    @Transactional
    public void updateGenreTagSpelling(String beforeName, String afterName){
        // 인증
        genreTagService.updateGenreTagSpelling(beforeName,afterName);
    }

    @Transactional
    public void createInstTag(InstrumentTagDto instrumentTagDto){
        //인증
        instrumentTagService.createInstrumentTag(instrumentTagDto);
    }

    @Transactional
    public void createMoodTag(MoodTagDto moodTagDto){
        //인증
        moodTagService.createMoodTag(moodTagDto);
    }

    @Transactional
    public void createGenreTag(GenreTagDto genreTagDto){
        //인증
        genreTagService.createGenreTag(genreTagDto);
    }
}

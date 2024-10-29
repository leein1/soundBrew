package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.InstrumentTagDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.repository.sound.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {
    @Autowired
    private MusicGenreTagRepository musicGenreTagRepository;
    @Autowired
    private MusicMoodTagRepository musicMoodTagRepository;
    @Autowired
    private MusicInstrumentTagRepository musicInstrumentTagRepository;

    @Autowired
    private InstrumentTagRepository instrumentTagRepository;
    @Autowired
    private MoodTagRepository moodTagRepository;
    @Autowired
    private GenreTagRepository genreTagRepository;


    public void updateMusicInstrumentTag(Music music,InstrumentTagDto instrumentTagDto){
        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());
        createMusicInstrumentTag(music,instrumentTagDto);

    }
    public void updateMusicMoodTag(Music music,MoodTagDto moodTagDto){
        musicMoodTagRepository.deleteByIdMusicId(music.getMusicId());
        createMusicMoodTag(music,moodTagDto);

    }
    public void updateMusicGenreTag(Music music,GenreTagDto genreTagDto){
        musicGenreTagRepository.deleteByIdMusicId(music.getMusicId());
        createMusicGenreTag(music, genreTagDto);
    }

    public void createMusicInstrumentTag(Music music, InstrumentTagDto instrumentTagDto){
        List<MusicInstrumentTag> instrumentTags = new ArrayList<>();
        for (String tagName : instrumentTagDto.getInstrument()) {
            InstrumentTag instrumentTag = instrumentTagRepository.findByInstrumentTagName(tagName).orElseThrow();

            MusicInstrumentTag musicInstrumentTag = MusicInstrumentTag.builder()
                    .music(music)
                    .instrumentTag(instrumentTag)
                    .id(new MusicInstrumentTagId(music.getMusicId(), instrumentTag.getInstrumentTagId()))
                    .build();
            instrumentTags.add(musicInstrumentTag);
        }
        musicInstrumentTagRepository.saveAll(instrumentTags);
    }

    public void createMusicMoodTag(Music music, MoodTagDto moodTagDto){
        List<MusicMoodTag> moodTags = new ArrayList<>();
        for(String tagName : moodTagDto.getMood()){
            MoodTag moodTag = moodTagRepository.findByMoodTagName(tagName).orElseThrow();

            MusicMoodTag musicMoodTag = MusicMoodTag.builder()
                    .id(new MusicMoodTagId( music.getMusicId(), moodTag.getMoodTagId()))
                    .moodTag(moodTag)
                    .music(music)
                    .build();
            moodTags.add(musicMoodTag);
        }
        musicMoodTagRepository.saveAll(moodTags);
    }

    public void createMusicGenreTag(Music music, GenreTagDto genreTagDto){
        List<MusicGenreTag> genreTags = new ArrayList<>();
        for(String tagName : genreTagDto.getGenre()){
            GenreTag genreTag = genreTagRepository.findByGenreTagName(tagName).orElseThrow();

            MusicGenreTag musicGenreTag = MusicGenreTag.builder()
                    .music(music)
                    .genreTag(genreTag)
                    .id(new MusicGenreTagId(music.getMusicId(), genreTag.getGenreTagId()))
                    .build();
            genreTags.add(musicGenreTag);
        }
        musicGenreTagRepository.saveAll(genreTags);
    }

    public void updateInstrumentTagSpelling(String beforeName, String afterName){
        InstrumentTag instrumentTag = instrumentTagRepository.findByInstrumentTagName(beforeName).orElseThrow();
        instrumentTag.update(afterName);
    }
    public void updateMoodTagSpelling(String beforeName, String afterName){
        MoodTag moodTag = moodTagRepository.findByMoodTagName(beforeName).orElseThrow();
        moodTag.update(afterName);

    }
    public void updateGenreTagSpelling(String beforeName, String afterName){
        GenreTag genreTag = genreTagRepository.findByGenreTagName(beforeName).orElseThrow();
        genreTag.update(afterName);
    }
}

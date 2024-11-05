package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTag;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTagId;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.repository.sound.MoodTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicMoodTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoodTagService {
    @Autowired
    private MusicMoodTagRepository musicMoodTagRepository;
    @Autowired
    private MoodTagRepository moodTagRepository;

    public void updateMusicMoodTag(Music music, MoodTagDto moodTagDto){
        musicMoodTagRepository.deleteByIdMusicId(music.getMusicId());
        plusMusicMoodTag(music,moodTagDto);
    }

    public void plusMusicMoodTag(Music music, MoodTagDto moodTagDto){
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

    public void updateMoodTagSpelling(String beforeName, String afterName){
        MoodTag moodTag = moodTagRepository.findByMoodTagName(beforeName).orElseThrow();
        moodTag.update(afterName);
    }

    public void createMoodTag(MoodTagDto moodTagDto){
        for(MoodTag moodTag : moodTagDto.toEntity()){
            moodTagRepository.save(moodTag);
        }
    }

    public MoodTagDto readMoodTag(){
        List<MoodTag> moodTags= moodTagRepository.findAll();
        MoodTagDto moodTagDto = new MoodTagDto();
        moodTagDto.setMood(moodTags.stream()
                .map(MoodTag::getMoodTagName)  // moodTagName 필드 추출
                .collect(Collectors.toList())); // List<String> 형태로 변환

        return moodTagDto;
    }

}

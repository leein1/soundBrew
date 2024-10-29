package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    public Music createMusic(MusicDto musicDto){
        Music music = musicDto.toEntity();
        return musicRepository.save(music);
    }

    public void updateMusic(Music music, MusicDto musicDto){
        music.update(musicDto.getTitle(),musicDto.getDescription(), musicDto.getSoundType());
    }

    public void deleteMusic(int musicId){
        musicRepository.deleteById(musicId);
    }

}

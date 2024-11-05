package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    public Music createMusic(MusicDto musicDto){
        Music music = musicDto.toEntity();
        return musicRepository.save(music);
    }

    public void updateMusic(int musicId, MusicDto musicDto){
        Music modify = musicRepository.findById(musicId).orElseThrow();
        modify.update(musicDto.getTitle(),musicDto.getDescription(), musicDto.getSoundType());
        // modify.update(Entity());
    }

    public void deleteMusic(int musicId){
        musicRepository.deleteById(musicId);
    }

    public List<MusicDto> readMusicWithUserId(int userId){
        List<Music> result = musicRepository.findByUserId(userId).orElseThrow();
        ModelMapper modelMapper = new ModelMapper();

        List<MusicDto> musicDtos = result.stream()
                .map(music -> modelMapper.map( music,MusicDto.class))
                .collect(Collectors.toList());
        return musicDtos;
    }

    public List<MusicDto> readMusic(){
        List<Music> music = musicRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        List<MusicDto> musicDtos = music
                .stream()
                .map(result -> modelMapper.map(result, MusicDto.class))
                .collect(Collectors.toList());

        return musicDtos;

    }
}

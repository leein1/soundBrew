package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.repository.sound.GenreTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicGenreTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class GenreTagService {
    @Autowired
    private MusicGenreTagRepository musicGenreTagRepository;
    @Autowired
    private GenreTagRepository genreTagRepository;

    public void updateMusicGenreTag(Music music, GenreTagDto genreTagDto){
        musicGenreTagRepository.deleteByIdMusicId(music.getMusicId());
        plusMusicGenreTag(music, genreTagDto);
    }

    public void plusMusicGenreTag(Music music, GenreTagDto genreTagDto){
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

    public void updateGenreTagSpelling(String before, String after){
        GenreTag genreTag= genreTagRepository.findByGenreTagName(before).orElseThrow();
        genreTag.update(after);
    }

    public void createGenreTag(GenreTagDto genreTagDto){
        for (GenreTag genreTag : genreTagDto.toEntityList()) {
            genreTagRepository.save(genreTag);
        }
    }

    public GenreTagDto readGenreTag(){
        List<GenreTag> genreTag= genreTagRepository.findAll();
        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(genreTag.stream()
                .map(GenreTag::getGenreTagName)
                .collect(Collectors.toList()));

        return genreTagDto;
    }

}

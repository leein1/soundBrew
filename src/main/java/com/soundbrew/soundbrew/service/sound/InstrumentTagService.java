package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTagId;
import com.soundbrew.soundbrew.dto.sound.InstrumentTagDto;
import com.soundbrew.soundbrew.repository.sound.InstrumentTagRepository;
import com.soundbrew.soundbrew.repository.sound.MusicInstrumentTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentTagService {
    @Autowired
    private MusicInstrumentTagRepository musicInstrumentTagRepository;
    @Autowired
    private InstrumentTagRepository instrumentTagRepository;

    public void updateMusicInstrumentTag(Music music, InstrumentTagDto instrumentTagDto){
        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());
        plusMusicInstrumentTag(music,instrumentTagDto);
    }

    public void plusMusicInstrumentTag(Music music, InstrumentTagDto instrumentTagDto){
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

    public void updateInstrumentTagSpelling(String before, String after){
        InstrumentTag instrumentTag = instrumentTagRepository.findByInstrumentTagName(before).orElseThrow();
        instrumentTag.update(after);
    }

    public void createInstrumentTag(InstrumentTagDto instrumentTagDto) {
        for(InstrumentTag instrumentTag : instrumentTagDto.toEntity()){
            instrumentTagRepository.save(instrumentTag);
        }
    }

    public InstrumentTagDto readInstrumentTag(){
        List<InstrumentTag> instrumentTag = instrumentTagRepository.findAll();
        InstrumentTagDto instrumentTagDto = new InstrumentTagDto();
        instrumentTagDto.setInstrument(instrumentTag.stream()
                .map(InstrumentTag ::getInstrumentTagName)
                .collect(Collectors.toList()));

        return instrumentTagDto;
    }
}

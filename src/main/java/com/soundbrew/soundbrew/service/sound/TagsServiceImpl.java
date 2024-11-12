package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.soundbrew.soundbrew.dto.sound.SoundFactory.*;

@Service
@AllArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;
    private final MusicInstrumentTagRepository musicInstrumentTagRepository;
    private final MusicMoodTagRepository musicMoodTagRepository;
    private final MusicGenreTagRepository musicGenreTagRepository;

    private final MusicRepository musicRepository;

    @Override
    public void createInstTag(TagsDto tagsDto) {
        for(InstrumentTag instrumentTag : tagsDto.InstToEntity()){
            instrumentTagRepository.save(instrumentTag);
        }
    }

    @Override
    public void createMoodTag(TagsDto tagsDto) {
        for(MoodTag moodTag : tagsDto.moodToEntity()){
            moodTagRepository.save(moodTag);
        }
    }

    @Override
    public void createGenreTag(TagsDto tagsDto) {
        for (GenreTag genreTag : tagsDto.genreToEntity()) {
            genreTagRepository.save(genreTag);
        }
    }

    @Override
    public void updateInstrumentTagSpelling(String beforeName, String afterName) {
        InstrumentTag instrumentTag = instrumentTagRepository.findByInstrumentTagName(beforeName).orElseThrow();
        instrumentTag.update(afterName);
    }

    @Override
    public void updateMoodTagSpelling(String beforeName, String afterName) {
        MoodTag moodTag = moodTagRepository.findByMoodTagName(beforeName).orElseThrow();
        moodTag.update(afterName);
    }

    @Override
    public void updateGenreTagSpelling(String beforeName, String afterName) {
        GenreTag genreTag= genreTagRepository.findByGenreTagName(beforeName).orElseThrow();
        genreTag.update(afterName);
    }

    @Override
    public MoodTagDto readMoodTag() {
        List<MoodTag> moodTags= moodTagRepository.findAll();
        MoodTagDto moodTagDto = new MoodTagDto();
        moodTagDto.setMood(moodTags.stream()
                .map(MoodTag::getMoodTagName)  // moodTagName 필드 추출
                .collect(Collectors.toList())); // List<String> 형태로 변환

        return moodTagDto;
    }

    @Override
    public GenreTagDto readGenreTag() {
        List<GenreTag> genreTag= genreTagRepository.findAll();
        GenreTagDto genreTagDto = new GenreTagDto();
        genreTagDto.setGenre(genreTag.stream()
                .map(GenreTag::getGenreTagName)
                .collect(Collectors.toList()));

        return genreTagDto;
    }

    @Override
    public TagsDto readInstTag() {
        List<InstrumentTag> instrumentTag = instrumentTagRepository.findAll();
        TagsDto tagsDto = new TagsDto();
        tagsDto.setInstrument(instrumentTag.stream()
                .map(InstrumentTag ::getInstrumentTagName)
                .collect(Collectors.toList()));

        return tagsDto;
    }

    @Override
    public void updateSoundTags(int musicId, TagsDto tagsDto) {
        Music music = musicRepository.findById(musicId).orElseThrow();
        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.getMusicId());

        createSoundTags(music,tagsDto);
    }

    @Override
    public void createSoundTags(Music music, TagsDto tagsDto) {
        // tagsDto를 process 클래스를 통해서 확인 과정 ....

         List<MusicInstrumentTag> instrumentTags = tagsDto.getInstrument().stream()
                .map(tagName -> instrumentTagRepository.findByInstrumentTagName(tagName).orElseThrow())
                .map(instrumentTag -> musicInstrumentTagToEntity(music, instrumentTag))
                .collect(Collectors.toList());

        List<MusicMoodTag> moodTags = tagsDto.getMood().stream()
                .map(tagName -> moodTagRepository.findByMoodTagName(tagName).orElseThrow())
                .map(moodTag -> musicMoodTagToEntity(music, moodTag))
                .collect(Collectors.toList());

        List<MusicGenreTag>genreTags = tagsDto.getGenre().stream()
                .map(tagName -> genreTagRepository.findByGenreTagName(tagName).orElseThrow())
                .map(genreTag -> musicGenreTagToEntity(music, genreTag))
                .collect(Collectors.toList());

        musicInstrumentTagRepository.saveAll(instrumentTags);
        musicGenreTagRepository.saveAll(genreTags);
        musicMoodTagRepository.saveAll(moodTags);
    }
}

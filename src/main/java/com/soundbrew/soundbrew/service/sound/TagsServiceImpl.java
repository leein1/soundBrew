package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.soundbrew.soundbrew.dto.BuilderFactory.*;

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
    @Transactional
    public void updateInstrumentTagSpelling(String beforeName, String afterName) {
        Optional<InstrumentTag> instrumentTag = instrumentTagRepository.findByInstrumentTagName(beforeName);
        if(instrumentTag.isEmpty()){return;}
        instrumentTag.get().update(afterName);
    }

    @Override
    @Transactional
    public void updateMoodTagSpelling(String beforeName, String afterName) {
        MoodTag moodTag = moodTagRepository.findByMoodTagName(beforeName).orElseThrow();
        moodTag.update(afterName);
    }

    @Override
    @Transactional
    public void updateGenreTagSpelling(String beforeName, String afterName) {
        GenreTag genreTag= genreTagRepository.findByGenreTagName(beforeName).orElseThrow();
        genreTag.update(afterName);
    }

    @Override
    @Transactional
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

    // 모든 태그들 들고오기 (for admin)
    @Override
    public ResponseDto<TagsDto> readTags() {
        TagsDto tagsDto = new TagsDto();

        List<MoodTag> moodTags= moodTagRepository.findAll();
        tagsDto.setMood(moodTags.stream()
                .map(MoodTag::getMoodTagName)  // moodTagName 필드 추출
                .collect(Collectors.toList())); // List<String> 형태로 변환

        List<InstrumentTag> instrumentTag = instrumentTagRepository.findAll();
        tagsDto.setInstrument(instrumentTag.stream()
                .map(InstrumentTag ::getInstrumentTagName)
                .collect(Collectors.toList()));

        List<GenreTag> genreTag= genreTagRepository.findAll();
        tagsDto.setGenre(genreTag.stream()
                .map(GenreTag::getGenreTagName)
                .collect(Collectors.toList()));

        return ResponseDto.<TagsDto>builder().dtoList(List.of(tagsDto)).build();
    }

    // musicId를 통해서 태그들 검색 (for artist or for admin)
    @Transactional
    public ResponseDto<TagsDto> readTagsByMusicIds(List<Integer> musicIds) {
        List<TagsDto> musicTagsDtos = new ArrayList<>();

        for (int musicId : musicIds) {
            List<MusicInstrumentTag> instrumentTags = musicInstrumentTagRepository.findByIdMusicId(musicId);
            List<String> instrumentTagNames = instrumentTags.stream()
                    .map(tag -> tag.getInstrumentTag().getInstrumentTagName())
                    .collect(Collectors.toList());

            List<MusicMoodTag> moodTags = musicMoodTagRepository.findByIdMusicId(musicId);
            List<String> moodTagNames = moodTags.stream()
                    .map(tag -> tag.getMoodTag().getMoodTagName())
                    .collect(Collectors.toList());

            List<MusicGenreTag> genreTags = musicGenreTagRepository.findByIdMusicId(musicId);
            List<String> genreTagNames = genreTags.stream()
                    .map(tag -> tag.getGenreTag().getGenreTagName())
                    .collect(Collectors.toList());

            String title = instrumentTags.isEmpty() ? null : instrumentTags.get(0).getMusic().getTitle();

            TagsDto tagsDto = TagsDto.builder()
                    .musicId(musicId)
                    .title(title)
                    .instrument(instrumentTagNames)
                    .mood(moodTagNames)
                    .genre(genreTagNames)
                    .build();

            musicTagsDtos.add(tagsDto);
        }

        return ResponseDto.<TagsDto>builder().dtoList(musicTagsDtos).build();
    }




}

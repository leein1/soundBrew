package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.repository.sound.*;
import com.soundbrew.soundbrew.service.authentication.SoundOwnershipCheckService;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.soundbrew.soundbrew.dto.BuilderFactory.*;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;
    private final MusicInstrumentTagRepository musicInstrumentTagRepository;
    private final MusicMoodTagRepository musicMoodTagRepository;
    private final MusicGenreTagRepository musicGenreTagRepository;
    private final MusicRepository musicRepository;
    private final SoundProcessor soundProcessor;
    private final SoundOwnershipCheckService soundOwnershipCheckService;


    @Override
    public ResponseDTO<TagsDTO> tagsSearchAfter(List<SearchTotalResultDTO> sounds) {
        TagsDTO tagsDTO = new TagsDTO();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SearchTotalResultDTO sound : sounds) {
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getInstrumentTagName(), instTagSet);
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getMoodTagName(), moodTagSet);
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getGenreTagName(), genreTagSet);
        }

        tagsDTO.setInstrument(new ArrayList<>(instTagSet));
        tagsDTO.setMood(new ArrayList<>(moodTagSet));
        tagsDTO.setGenre(new ArrayList<>(genreTagSet));

        return ResponseDTO.<TagsDTO>withSingleData().dto(tagsDTO).build();
    }

    @Override
    @Transactional
    public ResponseDTO<TagsDTO> getTags() {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setMood(moodTagRepository.findAll().stream()
                .map(MoodTag::getMoodTagName)
                .collect(Collectors.toList()));

        tagsDTO.setInstrument(instrumentTagRepository.findAll().stream()
                .map(InstrumentTag::getInstrumentTagName)
                .collect(Collectors.toList()));

        tagsDTO.setGenre(genreTagRepository.findAll().stream()
                .map(GenreTag::getGenreTagName)
                .collect(Collectors.toList()));

        return ResponseDTO.<TagsDTO>builder().dtoList(List.of(tagsDTO)).build();
    }

    @Override
    public ResponseDTO<TagsDTO> getAllTags(RequestDTO requestDTO){
        Optional<List<SearchTotalResultDTO>> result = musicRepository.getAllTags(requestDTO);
        System.out.println(result.get());
        if(result.get().isEmpty()) return ResponseDTO.<TagsDTO>withMessage().message("검색된 태그가 없습니다.").build();
        TagsDTO tagsDTO = new TagsDTO();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SearchTotalResultDTO sound : result.get()) {
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getInstrumentTagName(), instTagSet);
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getMoodTagName(), moodTagSet);
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getGenreTagName(), genreTagSet);
        }

        tagsDTO.setInstrument(new ArrayList<>(instTagSet));
        tagsDTO.setMood(new ArrayList<>(moodTagSet));
        tagsDTO.setGenre(new ArrayList<>(genreTagSet));

        return ResponseDTO.<TagsDTO>withSingleData().dto(tagsDTO).build();
    }

    private ResponseDTO updateLinkTags(Music music, TagsDTO tagsDTO) {
        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.getMusicId());

        return this.linkTags(music, tagsDTO);
    }

    @Transactional
    public ResponseDTO updateLinkTagsForArtist(int musicId, TagsDTO tagsDTO,int userId) {
        Music music = this.findByMusicId(musicId);

        soundOwnershipCheckService.checkMusicAccessById(music.getUserId(),userId);

        return updateLinkTags(music, tagsDTO);
    }

    @Transactional
    public ResponseDTO updateLinkTagsForAdmin(int musicId, TagsDTO tagsDTO) {
        Music music = this.findByMusicId(musicId);

        return updateLinkTags(music, tagsDTO);
    }

    @Override
    @Transactional
    public ResponseDTO linkTags(Music music, TagsDTO tagsDTO) {
        List<MusicInstrumentTag> instrumentTags = new ArrayList<>();
        List<MusicMoodTag> moodTags = new ArrayList<>();
        List<MusicGenreTag>genreTags = new ArrayList<>();

        for (String tagName : tagsDTO.getInstrument()) {
            InstrumentTag instrumentTag = this.findByInstrumentTagName(tagName);
            MusicInstrumentTag musicInstrumentTag = musicInstrumentTagToEntity(music, instrumentTag);
            instrumentTags.add(musicInstrumentTag);
        }

        for(String tagName : tagsDTO.getMood()){
            MoodTag moodTag  = this.findByMoodTagName(tagName);
            MusicMoodTag musicMoodTag = musicMoodTagToEntity(music,moodTag);
            moodTags.add(musicMoodTag);
        }

        for(String tagName : tagsDTO.getGenre()) {
            GenreTag genreTag = this.findByGenreTagName(tagName);
            MusicGenreTag musicGenreTag = musicGenreTagToEntity(music, genreTag);
            genreTags.add(musicGenreTag);
        }

        musicInstrumentTagRepository.saveAll(instrumentTags);
        musicGenreTagRepository.saveAll(genreTags);
        musicMoodTagRepository.saveAll(moodTags);

        return ResponseDTO.withMessage().message("음원의 태그를 새롭게 연결했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO createTag(TagsDTO tagsDTO) {// 반대로 태그가 있을떈?
        if (!tagsDTO.getInstrument().isEmpty()) tagsDTO.InstToEntity().forEach(instrumentTagRepository::save);
        if (!tagsDTO.getMood().isEmpty()) tagsDTO.moodToEntity().forEach(moodTagRepository::save);
        if (!tagsDTO.getGenre().isEmpty()) tagsDTO.genreToEntity().forEach(genreTagRepository::save);

        return ResponseDTO.withMessage().message("태그가 성공적으로 저장되었습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateInstrumentTagSpelling(String beforeName, TagsDTO afterName) {
        InstrumentTag instrumentTag = this.findByInstrumentTagName(beforeName);

        instrumentTag.update(afterName.getInstrument().get(0));
        return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateMoodTagSpelling(String beforeName, TagsDTO afterName) {
        MoodTag moodTag = this.findByMoodTagName(beforeName);

        moodTag.update(afterName.getMood().get(0));
        return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateGenreTagSpelling(String beforeName, TagsDTO afterName) {
        GenreTag genreTag= this.findByGenreTagName(beforeName);

        genreTag.update(afterName.getGenre().get(0));
        return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    private InstrumentTag findByInstrumentTagName(String keyword){
        return instrumentTagRepository.findByInstrumentTagName(keyword).orElseThrow();
    }

    private MoodTag findByMoodTagName(String keyword){
        return moodTagRepository.findByMoodTagName(keyword).orElseThrow();
    }

    private GenreTag findByGenreTagName(String keyword){
        return genreTagRepository.findByGenreTagName(keyword).orElseThrow();
    }

    private Music findByMusicId(int musicId){
        return musicRepository.findById(musicId).orElseThrow();
    }
}


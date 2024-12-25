package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {
    private final MeService meService;
    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;
    private final MusicInstrumentTagRepository musicInstrumentTagRepository;
    private final MusicMoodTagRepository musicMoodTagRepository;
    private final MusicGenreTagRepository musicGenreTagRepository;
    private final AlbumRepository albumRepository;
    private final MusicRepository musicRepository;

    @Override
    @Transactional
    public ResponseDTO createTag(TagsDTO tagsDTO) {
        if (!tagsDTO.getInstrument().isEmpty()) tagsDTO.InstToEntity().forEach(instrumentTagRepository::save);
        if (!tagsDTO.getMood().isEmpty()) tagsDTO.moodToEntity().forEach(moodTagRepository::save);
        if (!tagsDTO.getGenre().isEmpty()) tagsDTO.genreToEntity().forEach(genreTagRepository::save);

       return ResponseDTO.withMessage().message("태그가 성공적으로 저장되었습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateInstrumentTagSpelling(String beforeName, String afterName) {
        Optional<InstrumentTag> instrumentTag = instrumentTagRepository.findByInstrumentTagName(beforeName);
        if(instrumentTag.isEmpty()) return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하지 않았습니다.").build();

        instrumentTag.get().update(afterName);
        return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateMoodTagSpelling(String beforeName, String afterName) {
        Optional<MoodTag> moodTag = moodTagRepository.findByMoodTagName(beforeName);
        if(moodTag.isEmpty()) return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하지 않았습니다.").build();

        moodTag.get().update(afterName);
        return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateGenreTagSpelling(String beforeName, String afterName) {
        Optional<GenreTag> genreTag= genreTagRepository.findByGenreTagName(beforeName);
        if(genreTag.isEmpty()) return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하지 않았습니다.").build();

        genreTag.get().update(afterName);
        return ResponseDTO.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO<TagsDTO> getTags(List<Integer> musicIds) {
        if (musicIds == null || musicIds.isEmpty()) {
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
        } else {
            List<TagsDTO> musicTagsDTOS = musicIds.stream()
                    .map(musicId -> {
                        List<String> instrumentTagNames = musicInstrumentTagRepository.findByIdMusicId(musicId).stream()
                                .map(tag -> tag.getInstrumentTag().getInstrumentTagName())
                                .collect(Collectors.toList());

                        List<String> moodTagNames = musicMoodTagRepository.findByIdMusicId(musicId).stream()
                                .map(tag -> tag.getMoodTag().getMoodTagName())
                                .collect(Collectors.toList());

                        List<String> genreTagNames = musicGenreTagRepository.findByIdMusicId(musicId).stream()
                                .map(tag -> tag.getGenreTag().getGenreTagName())
                                .collect(Collectors.toList());

                        String title = instrumentTagNames.isEmpty() ? null
                                : musicInstrumentTagRepository.findByIdMusicId(musicId).get(0).getMusic().getTitle();

                        return TagsDTO.builder().musicId(musicId).title(title).instrument(instrumentTagNames).mood(moodTagNames).genre(genreTagNames).build();
                    }).collect(Collectors.toList());

            return ResponseDTO.<TagsDTO>builder().dtoList(musicTagsDTOS).build();
        }
    }

    @Override
    public ResponseDTO deleteAlbum(int albumId) {
        albumRepository.deleteById(albumId);
        return ResponseDTO.withMessage().message("삭제가 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDTO deleteMusic(int musicId) {
        musicRepository.deleteById(musicId);
        return ResponseDTO.withMessage().message("삭제가 정상적으로 처리되었습니다.").build();
    }

    @Transactional
    public ResponseDTO updateLinkTags(int musicId, TagsDTO tagsDTO) {
       return meService.updateLinkTags(musicId,tagsDTO);
    }

    @Transactional
    public ResponseDTO linkTags(Music music, TagsDTO tagsDTO) {
        return meService.linkTags(music,tagsDTO);
    }

    @Transactional
    public ResponseDTO updateAlbum(int albumId, AlbumDTO albumDTO) {
        return meService.updateAlbum(albumId,albumDTO);
    }

    @Transactional
    public ResponseDTO updateMusic(int musicId, MusicDTO musicDTO) {
        //verify도 수정가능해야함.
       return  meService.updateMusic(musicId, musicDTO);
    }

    public ResponseDTO<SearchTotalResultDTO> getSoundOne(int userId, int id) {
        //임의의 키워드
        return meService.getSoundOne(userId,id);
    }

    public ResponseDTO<SearchTotalResultDTO> getAlbumOne(int userId, int id) {
        // 임의의 키워드
        return meService.getSoundOne(userId, id);
    }
}

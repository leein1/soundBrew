package com.soundbrew.soundbrew.service.newservice;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.MusicDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soundbrew.soundbrew.dto.BuilderFactory.*;

@Service
@AllArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService{
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
    public ResponseDto createTag(TagsDto tagsDto) {
        if (!tagsDto.getInstrument().isEmpty()) {
            tagsDto.InstToEntity().forEach(instrumentTagRepository::save);
        }

        if (!tagsDto.getMood().isEmpty()) {
            tagsDto.moodToEntity().forEach(moodTagRepository::save);
        }

        if (!tagsDto.getGenre().isEmpty()) {
            tagsDto.genreToEntity().forEach(genreTagRepository::save);
        }

       return ResponseDto.withMessage().message("태그가 성공적으로 저장되었습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto updateInstrumentTagSpelling(String beforeName, String afterName) {
        Optional<InstrumentTag> instrumentTag = instrumentTagRepository.findByInstrumentTagName(beforeName);
        if(instrumentTag.isEmpty()) return ResponseDto.withMessage().message("오탈자 수정이 정상적으로 작동하지 않았습니다.").build();

        instrumentTag.get().update(afterName);
        return ResponseDto.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto updateMoodTagSpelling(String beforeName, String afterName) {
        Optional<MoodTag> moodTag = moodTagRepository.findByMoodTagName(beforeName);
        if(moodTag.isEmpty()) return ResponseDto.withMessage().message("오탈자 수정이 정상적으로 작동하지 않았습니다.").build();

        moodTag.get().update(afterName);
        return ResponseDto.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto updateGenreTagSpelling(String beforeName, String afterName) {
        Optional<GenreTag> genreTag= genreTagRepository.findByGenreTagName(beforeName);
        if(genreTag.isEmpty()) return ResponseDto.withMessage().message("오탈자 수정이 정상적으로 작동하지 않았습니다.").build();

        genreTag.get().update(afterName);
        return ResponseDto.withMessage().message("오탈자 수정이 정상적으로 작동하였습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto updateSoundTags(int musicId, TagsDto tagsDto) {
        Optional<Music> music = musicRepository.findById(musicId);
        if(music.isEmpty()) return ResponseDto.withMessage().message("업데이트 하려는 음원을 찾지 못했습니다.").build();

        musicInstrumentTagRepository.deleteByIdMusicId(music.get().getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.get().getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.get().getMusicId());

        linkSoundTags(music.get(),tagsDto);
        return ResponseDto.withMessage().message("음원의 태그를 새롭게 연결했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto linkSoundTags(Music music, TagsDto tagsDto) {
        List<MusicInstrumentTag> instrumentTags = new ArrayList<>();
        List<MusicMoodTag> moodTags = new ArrayList<>();
        List<MusicGenreTag>genreTags = new ArrayList<>();

        for (String tagName : tagsDto.getInstrument()) {
            Optional<InstrumentTag> instrumentTag = instrumentTagRepository.findByInstrumentTagName(tagName);
            if(!instrumentTag.isEmpty()) return ResponseDto.withMessage().message("악기 태그 연결에서 문제가 발생했습니다.").build();

            MusicInstrumentTag musicInstrumentTag = musicInstrumentTagToEntity(music, instrumentTag.get());
            instrumentTags.add(musicInstrumentTag);
        }

        for(String tagName : tagsDto.getMood()){
            Optional<MoodTag> moodTag  = moodTagRepository.findByMoodTagName(tagName);
            if(!moodTag.isEmpty()) return ResponseDto.withMessage().message("무드 태그 연결에서 문제가 발생했습니다.").build();

            MusicMoodTag musicMoodTag = musicMoodTagToEntity(music,moodTag.get());
            moodTags.add(musicMoodTag);
        }

        for(String tagName : tagsDto.getGenre()){
            Optional<GenreTag> genreTag = genreTagRepository.findByGenreTagName(tagName);
            if(!genreTag.isEmpty()) return ResponseDto.withMessage().message("장르 태그 연결에서 문제가 발생했습니다.").build();

            MusicGenreTag musicGenreTag = musicGenreTagToEntity(music,genreTag.get());
            genreTags.add(musicGenreTag);

        }

        musicInstrumentTagRepository.saveAll(instrumentTags);
        musicGenreTagRepository.saveAll(genreTags);
        musicMoodTagRepository.saveAll(moodTags);

        return ResponseDto.withMessage().message("음원의 태그를 새롭게 연결했습니다.").build();
    }

    @Transactional
    public ResponseDto<TagsDto> getTags(List<Integer> musicIds) {
        if (musicIds == null || musicIds.isEmpty()) {
            TagsDto tagsDto = new TagsDto();

            tagsDto.setMood(moodTagRepository.findAll().stream()
                    .map(MoodTag::getMoodTagName)
                    .collect(Collectors.toList()));

            tagsDto.setInstrument(instrumentTagRepository.findAll().stream()
                    .map(InstrumentTag::getInstrumentTagName)
                    .collect(Collectors.toList()));

            tagsDto.setGenre(genreTagRepository.findAll().stream()
                    .map(GenreTag::getGenreTagName)
                    .collect(Collectors.toList()));

            return ResponseDto.<TagsDto>builder().dtoList(List.of(tagsDto)).build();
        } else {
            List<TagsDto> musicTagsDtos = musicIds.stream()
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

                        return TagsDto.builder().musicId(musicId).title(title).instrument(instrumentTagNames).mood(moodTagNames).genre(genreTagNames).build();
                    }).collect(Collectors.toList());

            return ResponseDto.<TagsDto>builder().dtoList(musicTagsDtos).build();
        }
    }

////     @Override
////    public ResponseDto<SearchTotalResultDto> getUsersAlbums(String nickname, String title) {
////        Optional<SearchTotalResultDto> albumPage = albumRepository.searchAll(nickname, title);
////        if(albumPage.isPresent()) return ResponseDto.<SearchTotalResultDto>withMessage().message("찾으시는 앨범이 없습니다.").build();
////
////        return Collections.emptyList() ;
////    }
//
//    @Override
//    public ResponseDto<SearchTotalResultDto> getUsersSounds(String nickname, String title){
//        Optional<SearchTotalResultDto> musicPage = musicRepository.soundOne(nickname,title);
//        if(musicPage.isPresent()) return ResponseDto.<SearchTotalResultDto>withMessage().message("찾으시는 음원이 없습니다.").build();
//
//        return ResponseDto.<SearchTotalResultDto>withSingleData().dto(musicPage.get()).build();
//    }

    @Override
    public ResponseDto deleteAlbum(int albumId) {
        albumRepository.deleteById(albumId);
        return ResponseDto.withMessage().message("삭제가 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDto deleteMusic(int musicId) {
        musicRepository.deleteById(musicId);
        return ResponseDto.withMessage().message("삭제가 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDto updateAlbum(int albumId, AlbumDto albumDto) {
        Optional<Album> modify = albumRepository.findById(albumId);
        if (modify.isEmpty()) return ResponseDto.withMessage().message("수정할 대상이 없습니다.").build();

        modify.get().update(albumDto.getAlbumName(), albumDto.getDescription());
        return ResponseDto.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDto updateMusic(int musicId, MusicDto musicDto) {
        Optional<Music> modify = musicRepository.findById(musicId);
        if (modify.isEmpty()) return ResponseDto.withMessage().message("수정할 대상이 없습니다.").build();

        modify.get().update(musicDto.getTitle(),musicDto.getDescription(), musicDto.getSoundType());
        return ResponseDto.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }
}

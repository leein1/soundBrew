package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.soundbrew.soundbrew.dto.BuilderFactory.*;

@Service
@AllArgsConstructor
@Log4j2
public class MeServiceImpl implements MeService{
    private final UserRepository userRepository;

    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;
    private final MusicInstrumentTagRepository musicInstrumentTagRepository;
    private final MusicMoodTagRepository musicMoodTagRepository;
    private final MusicGenreTagRepository musicGenreTagRepository;
    private final AlbumRepository albumRepository;
    private final AlbumMusicRepository albumMusicRepository;
    private final MusicRepository musicRepository;

    @Override
    @Transactional
    public ResponseDto updateLinkTags(int musicId, TagsDto tagsDto) {
        Optional<Music> music = musicRepository.findById(musicId);
        if(music.isEmpty()) return ResponseDto.withMessage().message("업데이트 하려는 음원을 찾지 못했습니다.").build();

        musicInstrumentTagRepository.deleteByIdMusicId(music.get().getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.get().getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.get().getMusicId());

        linkTags(music.get(),tagsDto);
        return ResponseDto.withMessage().message("음원의 태그를 새롭게 연결했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto linkTags(Music music, TagsDto tagsDto) {
        List<MusicInstrumentTag> instrumentTags = new ArrayList<>();
        List<MusicMoodTag> moodTags = new ArrayList<>();
        List<MusicGenreTag>genreTags = new ArrayList<>();

        for (String tagName : tagsDto.getInstrument()) {
            Optional<InstrumentTag> instrumentTag = instrumentTagRepository.findByInstrumentTagName(tagName);
            if(instrumentTag.isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResponseDto.withMessage().message("악기 태그 연결에서 문제가 발생했습니다.").build();
            }

            MusicInstrumentTag musicInstrumentTag = musicInstrumentTagToEntity(music, instrumentTag.get());
            instrumentTags.add(musicInstrumentTag);
        }

        for(String tagName : tagsDto.getMood()){
            Optional<MoodTag> moodTag  = moodTagRepository.findByMoodTagName(tagName);
            if(moodTag.isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResponseDto.withMessage().message("무드 태그 연결에서 문제가 발생했습니다.").build();
            }

            MusicMoodTag musicMoodTag = musicMoodTagToEntity(music,moodTag.get());
            moodTags.add(musicMoodTag);
        }

        for(String tagName : tagsDto.getGenre()) {
            Optional<GenreTag> genreTag = genreTagRepository.findByGenreTagName(tagName);
            if (genreTag.isEmpty()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResponseDto.withMessage().message("장르 태그 연결에서 문제가 발생했습니다.").build();
            }

            MusicGenreTag musicGenreTag = musicGenreTagToEntity(music, genreTag.get());
            genreTags.add(musicGenreTag);
        }

        musicInstrumentTagRepository.saveAll(instrumentTags);
        musicGenreTagRepository.saveAll(genreTags);
        musicMoodTagRepository.saveAll(moodTags);

        return ResponseDto.withMessage().message("음원의 태그를 새롭게 연결했습니다.").build();
    }

    @Transactional
    @Override
    public ResponseDto createSound(int checkedUserId,AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto){
        Optional<User> checkedUser = userRepository.findById(2);
        if(checkedUser.isEmpty()) return ResponseDto.withMessage().message("회원정보가 올바르지 않습니다.").build();

        albumDto.setUserId(checkedUser.get().getUserId());
        musicDto.setUserId(checkedUser.get().getUserId());
        musicDto.setPrice(musicDto.getSoundType().equals("sfx") ? 2 : 3);
        Album album = albumRepository.save(albumDto.toEntity());
        Music music = musicRepository.save(musicDto.toEntity());
        albumMusicRepository.save(albumMusicToEntity(album,music,checkedUser.get()));
        linkTags(music,tagsDto);

        return ResponseDto.withMessage().message("정상적으로 등록했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto updateAlbum(int albumId, AlbumDto albumDto) {
        Optional<Album> modify = albumRepository.findById(albumId);
        if (modify.isEmpty()) return ResponseDto.withMessage().message("수정할 대상이 없습니다.").build();

        modify.get().update(albumDto.getAlbumName(), albumDto.getDescription());
        return ResponseDto.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDto updateMusic(int musicId, MusicDto musicDto) {
        Optional<Music> modify = musicRepository.findById(musicId);
        if (modify.isEmpty()) return ResponseDto.withMessage().message("수정할 대상이 없습니다.").build();

        modify.get().update(musicDto.getTitle(),musicDto.getDescription(), musicDto.getSoundType());
        return ResponseDto.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getSoundOne(String nickname, int id) {
        return null;
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getAlbumOne(String nickname, int id) {
        return null;
    }


}

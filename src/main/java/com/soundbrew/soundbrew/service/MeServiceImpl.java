package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.DTOFilteringFactory;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.*;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.soundbrew.soundbrew.dto.BuilderFactory.*;

@Service
@AllArgsConstructor
@Log4j2
public class MeServiceImpl implements MeService{
    private final ModelMapper modelMapper;

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
    private final SoundProcessor soundProcessor;

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
        albumDto.setNickname(checkedUser.get().getNickname());
        musicDto.setUserId(checkedUser.get().getUserId());
        musicDto.setNickname(checkedUser.get().getNickname());
        musicDto.setSoundType("sound");
        musicDto.setPrice(3);
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

        modify.get().update(musicDto.getTitle(),musicDto.getDescription(), modify.get().getSoundType());

//        Music music = modify.get();
//        MusicDto existingDto= modelMapper.map(music, MusicDto.class);
//        existingDto.setTitle("Ho Go Dong no.12");
//        Music updatedMusic = modelMapper.map(existingDto, Music.class);
//        updatedMusic.setMusicGenreTag(music.getMusicGenreTag());
//        updatedMusic.setMusicMoodTag(music.getMusicMoodTag());
//        updatedMusic.setMusicInstrumentTag(music.getMusicInstrumentTag());
//        updatedMusic.setAlbumMusics(music.getAlbumMusics());
//        musicRepository.save(updatedMusic);
        return ResponseDto.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getSoundOne(int userId, int id) {
        Optional<SearchTotalResultDto> soundOne = musicRepository.soundOne(userId,id);
        if(soundOne.isEmpty()) return ResponseDto.<SearchTotalResultDto>withMessage().message("찾으시는 음원이 없습니다.").build();

        return ResponseDto.<SearchTotalResultDto>withSingleData().dto(soundOne.get()).build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getAlbumOne(int userId, int id, RequestDto requestDto) {
        Optional<Page<SearchTotalResultDto>> albumOne = albumMusicRepository.albumOne(userId,id,requestDto);
        if(albumOne.get().isEmpty()) return ResponseDto.<SearchTotalResultDto>withMessage().message("찾으시는 앨범이 없습니다.").build();

        return ResponseDto.<SearchTotalResultDto>withAll().total((int) albumOne.get().getTotalElements()).requestDto(requestDto).dtoList(albumOne.get().getContent()).build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getSoundMe(RequestDto requestDto) {
        Optional<Page<SearchTotalResultDto>> before = albumMusicRepository.search(requestDto);
        if(before.get().isEmpty()) return ResponseDto.<SearchTotalResultDto>builder().dtoList(Collections.emptyList()).build();

        return ResponseDto.<SearchTotalResultDto>withAll()
                .requestDto(requestDto)
                .dtoList(soundProcessor.replaceCommaWithSpace(before.get().getContent()).stream().toList())
                .total((int) before.get().getTotalElements())
                .build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getAlbumMe(RequestDto responseDto) {
        Optional<Page<SearchTotalResultDto>> before = albumMusicRepository.searchAlbum(responseDto);
        if(before.get().isEmpty()) return ResponseDto.<SearchTotalResultDto>builder().dtoList(Collections.emptyList()).build();

        log.info(before.get().stream().toList());

        return  ResponseDto.<SearchTotalResultDto>withAll()
                .dtoList(before.get().getContent().stream().toList())
                .requestDto(responseDto)
                .build();
    }
}

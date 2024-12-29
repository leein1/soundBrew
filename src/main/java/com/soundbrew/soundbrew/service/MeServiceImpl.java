package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.user.UserRepository;
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
import java.util.stream.Collectors;

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

    private Music findByMusicId(int musicId){return musicRepository.findById(musicId).orElseThrow();}

    private Album findByAlbumId(int albumId){return albumRepository.findById(albumId).orElseThrow();}

    private InstrumentTag findByInstrumentTagName(String keyword){return instrumentTagRepository.findByInstrumentTagName(keyword).orElseThrow();}

    private MoodTag findByMoodTagName(String keyword){return moodTagRepository.findByMoodTagName(keyword).orElseThrow();}

    private GenreTag findByGenreTagName(String keyword){return genreTagRepository.findByGenreTagName(keyword).orElseThrow();}

    private User findByUserId(int userId){return userRepository.findById(userId).orElseThrow();}

    @Override
    @Transactional
    public ResponseDTO updateLinkTags(int musicId, TagsDTO tagsDTO) {
        Music music = this.findByMusicId(musicId);

        musicInstrumentTagRepository.deleteByIdMusicId(music.getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.getMusicId());

        return this.linkTags(music, tagsDTO);
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

    @Transactional
    @Override
    public ResponseDTO createSound(int checkedUserId, AlbumDTO albumDTO, MusicDTO musicDTO, TagsDTO tagsDTO){
        User checkedUser = this.findByUserId(checkedUserId);

        albumDTO.setUserId(checkedUser.getUserId());
        albumDTO.setNickname(checkedUser.getNickname());
        musicDTO.setUserId(checkedUser.getUserId());
        musicDTO.setNickname(checkedUser.getNickname());
        musicDTO.setSoundType("sound"); // db 디폴트 설정
        musicDTO.setPrice(3); // db 디폴트 설정
        Album album = albumRepository.save(albumDTO.toEntity());
        Music music = musicRepository.save(musicDTO.toEntity());
        albumMusicRepository.save(albumMusicToEntity(album,music,checkedUser));
        linkTags(music, tagsDTO);

        return ResponseDTO.withMessage().message("정상적으로 등록했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateAlbum(int albumId, AlbumDTO albumDTO) {
        Album modify = this.findByAlbumId(albumId);

        modify.update(albumDTO.getAlbumName(), albumDTO.getDescription());

        return ResponseDTO.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateMusic(int musicId, MusicDTO musicDTO) {
        Music modify = this.findByMusicId(musicId);

        modify.update(musicDTO.getTitle(), musicDTO.getDescription(), modify.getSoundType());

        return ResponseDTO.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundOne(int userId, int id) {
        Optional<SearchTotalResultDTO> soundOne = musicRepository.soundOne(userId,id);
        if(soundOne.isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 음원이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withSingleData().dto(soundOne.get()).build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumOne(int userId, int id, RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> albumOne = albumMusicRepository.albumOne(userId,id, requestDTO);
        if(albumOne.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 앨범이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,albumOne.get().getContent(), (int) albumOne.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundMe(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.search(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumMe(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.searchAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return  ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
    }
}

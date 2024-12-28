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
    public ResponseDTO updateLinkTags(int musicId, TagsDTO tagsDTO) {
        Optional<Music> music = musicRepository.findById(musicId);
        if(music.isEmpty()) return ResponseDTO.withMessage().message("업데이트 하려는 음원을 찾지 못했습니다.").build();

        musicInstrumentTagRepository.deleteByIdMusicId(music.get().getMusicId());
        musicMoodTagRepository.deleteByIdMusicId(music.get().getMusicId());
        musicGenreTagRepository.deleteByIdMusicId(music.get().getMusicId());

        linkTags(music.get(), tagsDTO);
            return ResponseDTO.withMessage().message("음원의 태그를 새롭게 연결했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO linkTags(Music music, TagsDTO tagsDTO) {
        List<MusicInstrumentTag> instrumentTags = new ArrayList<>();
        List<MusicMoodTag> moodTags = new ArrayList<>();
        List<MusicGenreTag>genreTags = new ArrayList<>();

        for (String tagName : tagsDTO.getInstrument()) {
            Optional<InstrumentTag> instrumentTag = instrumentTagRepository.findByInstrumentTagName(tagName);
            if(instrumentTag.isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResponseDTO.withMessage().message("악기 태그 연결에서 문제가 발생했습니다.").build();
            }

            MusicInstrumentTag musicInstrumentTag = musicInstrumentTagToEntity(music, instrumentTag.get());
            instrumentTags.add(musicInstrumentTag);
        }

        for(String tagName : tagsDTO.getMood()){
            Optional<MoodTag> moodTag  = moodTagRepository.findByMoodTagName(tagName);
            if(moodTag.isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResponseDTO.withMessage().message("무드 태그 연결에서 문제가 발생했습니다.").build();
            }

            MusicMoodTag musicMoodTag = musicMoodTagToEntity(music,moodTag.get());
            moodTags.add(musicMoodTag);
        }

        for(String tagName : tagsDTO.getGenre()) {
            Optional<GenreTag> genreTag = genreTagRepository.findByGenreTagName(tagName);
            if (genreTag.isEmpty()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResponseDTO.withMessage().message("장르 태그 연결에서 문제가 발생했습니다.").build();
            }

            MusicGenreTag musicGenreTag = musicGenreTagToEntity(music, genreTag.get());
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
        Optional<User> checkedUser = userRepository.findById(2);
        if(checkedUser.isEmpty()) return ResponseDTO.withMessage().message("회원정보가 올바르지 않습니다.").build();

        albumDTO.setUserId(checkedUser.get().getUserId());
        albumDTO.setNickname(checkedUser.get().getNickname());
        musicDTO.setUserId(checkedUser.get().getUserId());
        musicDTO.setNickname(checkedUser.get().getNickname());
        musicDTO.setSoundType("sound");
        musicDTO.setPrice(3);
        Album album = albumRepository.save(albumDTO.toEntity());
        Music music = musicRepository.save(musicDTO.toEntity());
        albumMusicRepository.save(albumMusicToEntity(album,music,checkedUser.get()));
        linkTags(music, tagsDTO);

        return ResponseDTO.withMessage().message("정상적으로 등록했습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateAlbum(int albumId, AlbumDTO albumDTO) {
        Optional<Album> modify = albumRepository.findById(albumId);
        if (modify.isEmpty()) return ResponseDTO.withMessage().message("수정할 대상이 없습니다.").build();

        modify.get().update(albumDTO.getAlbumName(), albumDTO.getDescription());

        return ResponseDTO.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    @Transactional
    public ResponseDTO updateMusic(int musicId, MusicDTO musicDTO) {
        Optional<Music> modify = musicRepository.findById(musicId);
        if (modify.isEmpty()) return ResponseDTO.withMessage().message("수정할 대상이 없습니다.").build();

        modify.get().update(musicDTO.getTitle(), musicDTO.getDescription(), modify.get().getSoundType());

//        Music music = modify.get();
//        MusicDTO existingDto= modelMapper.map(music, MusicDTO.class);
//        existingDto.setTitle("Ho Go Dong no.12");
//        Music updatedMusic = modelMapper.map(existingDto, Music.class);
//        updatedMusic.setMusicGenreTag(music.getMusicGenreTag());
//        updatedMusic.setMusicMoodTag(music.getMusicMoodTag());
//        updatedMusic.setMusicInstrumentTag(music.getMusicInstrumentTag());
//        updatedMusic.setAlbumMusics(music.getAlbumMusics());
//        musicRepository.save(updatedMusic);
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

        return null;
//        return ResponseDTO.<SearchTotalResultDTO>withAll().total((int) albumOne.get().getTotalElements()).requestDTO(requestDTO).dtoList(albumOne.get().getContent()).build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundMe(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.search(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return null;
//        return ResponseDTO.<SearchTotalResultDTO>withAll()
//                .requestDTO(requestDTO)
//                .dtoList(soundProcessor.replaceCommaWithSpace(before.get().getContent()).stream().toList())
//                .total((int) before.get().getTotalElements())
//                .build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumMe(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.searchAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        log.info(before.get().stream().toList());

        return null;
/*        return  ResponseDTO.<SearchTotalResultDTO>withAll()
                .dtoList(before.get().getContent().stream().toList())
                .requestDTO(requestDTO)
                .build();*/
    }
}

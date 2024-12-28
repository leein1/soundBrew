package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.dto.UserDetailsDTO;
import com.soundbrew.soundbrew.dto.sound.AlbumDTO;
import com.soundbrew.soundbrew.dto.sound.MusicDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final MeService meService;
    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;
    private final MusicInstrumentTagRepository musicInstrumentTagRepository;
    private final MusicMoodTagRepository musicMoodTagRepository;
    private final MusicGenreTagRepository musicGenreTagRepository;
    private final AlbumRepository albumRepository;
    private final MusicRepository musicRepository;
    private final AlbumMusicRepository albumMusicRepository;

    @Override
    public ResponseDTO<UserDetailsDTO> getAlluser(RequestDTO requestDTO) {

        Optional<Page<UserDetailsDTO>> result = userRepository.findAllUserDetails(requestDTO);

        if(result.get().isEmpty()){
            List<UserDetailsDTO> userDetailsDTOList = Collections.emptyList();
            return ResponseDTO.<UserDetailsDTO>withAll(requestDTO, userDetailsDTOList,0);

        }

        return ResponseDTO.<UserDetailsDTO>withAll(requestDTO,result.get().getContent(),(int)result.get().getTotalElements());
    }

    @Override
    public ResponseDTO<UserDetailsDTO> getUser(int userId) {

        UserDetailsDTO userDetialsDTO = userRepository.findUserDetailsById(userId).orElseThrow();

        ResponseDTO<UserDetailsDTO> responseDTO = ResponseDTO.<UserDetailsDTO>withSingleData()
                .dto(userDetialsDTO)
                .build();

        return responseDTO;
    }


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

    @Override
    @Transactional
    public ResponseDTO updateVerifyAlbum(int albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow();

        album.verify(1);
        return ResponseDTO.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> readVerifyAlbum(RequestDTO requestDTO){
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.verifyAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return null;
//        return  ResponseDTO.<SearchTotalResultDTO>withAll()
//                .dtoList(before.get().getContent().stream().toList())
//                .requestDTO(requestDTO)
//                .build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> readVerifyAlbumOne(int userId, int id, RequestDTO requestDTO){
        Optional<Page<SearchTotalResultDTO>> albumOne = albumMusicRepository.verifyAlbumOne(userId,id, requestDTO);
        if(albumOne.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 앨범이 없습니다.").build();

        return null;
//        return ResponseDTO.<SearchTotalResultDTO>withAll().total((int) albumOne.get().getTotalElements()).requestDTO(requestDTO).dtoList(albumOne.get().getContent()).build();
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

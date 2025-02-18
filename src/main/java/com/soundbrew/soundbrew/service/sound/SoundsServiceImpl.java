package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.sound.*;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.service.tag.TagsService;
import com.soundbrew.soundbrew.service.authentication.SoundOwnershipCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.soundbrew.soundbrew.dto.DTOFilteringFactory.hideSearchTotalResultDTO;

@Service
@RequiredArgsConstructor
@Log4j2
public class SoundsServiceImpl implements SoundsService{
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final AlbumMusicRepository albumMusicRepository;
    private final MusicRepository musicRepository;
    private final TagsService tagsService;
    private final SoundOwnershipCheckService soundOwnershipCheckService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseDTO<SearchTotalResultDTO> totalSoundSearch(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.search(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> totalAlbumSearch(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.searchAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return  ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundOne(String nickname, String title) {
        Optional<SearchTotalResultDTO> musicPage = musicRepository.soundOne(nickname,title);
        if(musicPage.isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 음원이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withSingleData().dto(hideSearchTotalResultDTO(musicPage.get())).build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumOne(String nickname, String albumName, RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> albumPage = albumMusicRepository.albumOne(nickname,albumName, requestDTO);
        if(albumPage.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 앨범의 정보가 없습니다.").build();

        return  ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,albumPage.get().getContent(), (int) albumPage.get().getTotalElements());
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
//        Album album = albumRepository.save(albumDTO.toEntity());
        Music music = musicRepository.save(musicDTO.toEntity());
//        albumMusicRepository.save(albumMusicToEntity(album,music,checkedUser));
        tagsService.linkTags(music, tagsDTO);

        return ResponseDTO.withMessage().message("정상적으로 등록했습니다.").build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundMe(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.search(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundOne(int userId, int id) {
        Optional<SearchTotalResultDTO> soundOne = musicRepository.soundOne(userId,id);
        if(soundOne.isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 음원이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withSingleData().dto(soundOne.get()).build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumOne(int userId, int id, RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> albumOne= albumMusicRepository.albumOne(userId,id,requestDTO);
        if(albumOne.isEmpty()) return  ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 앨범이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,albumOne.get().getContent(), (int) albumOne.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumMe(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.searchAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return  ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
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
        Album album = this.findByAlbumId(albumId);

        album.verify(1);

        return ResponseDTO.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> readVerifyAlbum(RequestDTO requestDTO){
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.verifyAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return  ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,before.get().getContent(), (int) before.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> readVerifyAlbumOne(int userId, int id, RequestDTO requestDTO){
        Optional<Page<SearchTotalResultDTO>> albumOne = albumMusicRepository.verifyAlbumOne(userId,id, requestDTO);
        if(albumOne.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 앨범이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withAll(requestDTO,albumOne.get().getContent(), (int) albumOne.get().getTotalElements());
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundOneForAdmin(int userId, int id) {
        return this.getSoundOne(userId,id);
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumOneForAdmin(int userId, int id,RequestDTO requestDTO) {
        return this.getAlbumOne(userId,id,requestDTO);
    }

    // 아티스트용: 소유권 검증을 수행한 후 업데이트
    @Override
    @Transactional
    public ResponseDTO updateAlbumForArtist(int albumId, AlbumDTO albumDTO, int userId) {
        Album album = this.findByAlbumId(albumId);
        soundOwnershipCheckService.checkAlbumAccessById(album.getUserId(), userId); // 검증 실패 시 예외 발생 등

        return updateAlbumInternal(album, albumDTO);
    }

    // 어드민용: 소유권 검증 없이 바로 업데이트
    @Override
    @Transactional
    public ResponseDTO updateAlbumForAdmin(int albumId, AlbumDTO albumDTO) {
        Album album = this.findByAlbumId(albumId);

        return updateAlbumInternal(album, albumDTO);
    }

    // 어드민용: 소유권 검증 없이 바로 업데이트
    @Override
    @Transactional
    public ResponseDTO updateMusicForAdmin(int musicId, MusicDTO musicDTO) {
        Music music = this.findByMusicId(musicId);
        return updateMusicInternal(music ,musicDTO);
    }

    // 아티스트용: 소유권 검증을 수행한 후 업데이트
    @Override
    @Transactional
    public ResponseDTO updateMusicForArtist(int musicId, MusicDTO musicDTO, int userId) {
        Music music = this.findByMusicId(musicId);
        soundOwnershipCheckService.checkMusicAccessById(music.getUserId(),userId);
        return updateMusicInternal(music,musicDTO);
    }

    // 실제 앨범 정보를 업데이트하는 공통 메서드
    private ResponseDTO updateAlbumInternal(Album album, AlbumDTO albumDTO) {
//        AlbumDTO select = modelMapper.map(album,AlbumDTO.class);
//        select.setDescription(albumDTO.getDescription());
//        select.setAlbumName(albumDTO.getAlbumName());
//        albumRepository.save(select.toEntity(album.getAlbumMusic()));
        album.update(albumDTO.getAlbumName(),albumDTO.getDescription());

        return ResponseDTO.withMessage()
                .message("변경이 정상적으로 처리되었습니다.")
                .build();
    }

    // 실제 음원 정보를 업데이트하는 공통 메서드
    private ResponseDTO updateMusicInternal(Music music, MusicDTO musicDTO) {
//        MusicDTO select = modelMapper.map(music , MusicDTO.class);
//        music.setDescription(musicDTO.getDescription());
//        music.setTitle(musicDTO.getTitle());
//        musicRepository.save(select.toEntity());
        music.update(musicDTO.getTitle(),musicDTO.getDescription(),music.getSoundType());

        return ResponseDTO.withMessage().message("변경이 정상적으로 처리되었습니다.").build();
    }

    private Music findByMusicId(int musicId){
        return musicRepository.findById(musicId).orElseThrow();
    }

    private Album findByAlbumId(int albumId){
        return albumRepository.findById(albumId).orElseThrow();
    }

    private User findByUserId(int userId){
        return userRepository.findById(userId).orElseThrow();
    }

}

package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.AlbumMusicRepository;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import com.soundbrew.soundbrew.repository.sound.custom.AlbumRepositoryCustomImpl;
import com.soundbrew.soundbrew.repository.sound.custom.MusicRepositoryCustomImpl;
import com.soundbrew.soundbrew.dto.DTOFilteringFactory;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.soundbrew.soundbrew.dto.BuilderFactory.albumMusicToEntity;

@Service
@AllArgsConstructor
@Log4j2
public class SoundServiceImpl implements SoundService{
    private final AlbumMusicRepository albumMusicRepository;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final SoundProcessor soundProcessor;
    private final TagsServiceImpl tagsServiceImpl;
    private final AlbumRepositoryCustomImpl albumRepositoryCustom;
    private final MusicRepositoryCustomImpl musicRepositoryCustom;

    @Override
    public ResponseDto<SearchResponseDto> totalSoundSearch(RequestDto requestDto) {
        Optional<Page<SearchTotalResultDto>> before = albumMusicRepository.search(requestDto);
        if(before.get().isEmpty()) return ResponseDto.<SearchResponseDto>builder().dto(Collections.emptyList()).build();

        List<SearchTotalResultDto> result = before.get().getContent();
        SearchResponseDto after = soundProcessor.replaceTagsToArray(result);
        List<SearchTotalResultDto> afterSearch = soundProcessor.replaceCommaWithSpace(result);
        after.setSearchTotalResultDto(afterSearch);

        return new ResponseDto<>(requestDto,List.of(after),(int) before.get().getTotalElements());
    }

    @Override
    public ResponseDto<SearchAlbumResultDto> totalAlbumSearch(RequestDto searchRequestDto) {
        Optional<Page<SearchAlbumResultDto>> before = albumMusicRepository.albums(searchRequestDto);
        if(before.get().isEmpty()) return ResponseDto.<SearchAlbumResultDto>builder().dto(Collections.emptyList()).build();

        List<SearchAlbumResultDto> result = before.get().getContent();
        return new ResponseDto<>(searchRequestDto,result,(int) before.get().getTotalElements());
    }

    @Override // 숨김정보 x 본인 확인 o
    public ResponseDto<AlbumDto> getUsersAlbums(RequestDto requestDto) {
        Optional<Page<AlbumDto>> albumPage = albumRepositoryCustom.searchAll(requestDto.getType(), requestDto.getKeyword(), requestDto.getPageable("albumId"));
        if(albumPage.get().isEmpty()) return ResponseDto.<AlbumDto>builder().dto(Collections.emptyList()).build();

        List<AlbumDto> listDto = albumPage.get().getContent();

        // ResponseDto 생성 및 반환
        return new ResponseDto<>(requestDto, listDto, (int) albumPage.get().getTotalElements());
    }

    // 분기점 1. 나의 음원 보기 -> 나의 음원을 검색하는지 확인
    // 분기점 2. 어드민의 회원들 음원 보기 -> 어드민인지 확인
    @Override
    public ResponseDto<MusicDto> getUsersSounds(RequestDto requestDto){
        Optional<Page<MusicDto>> musicPage = musicRepositoryCustom.searchAll(requestDto.getType(),requestDto.getKeyword(),requestDto.getPageable("albumId"));
        if(musicPage.get().isEmpty()) return ResponseDto.<MusicDto>builder().dto(Collections.emptyList()).build();

        List<MusicDto> listDto = musicPage.get().getContent();
        // 어드민 이라면

        // 어드민 아니라면(나의 음원 보기) - 아티스트라면
//        listDto.forEach(DTOFilteringFactory::hideMusicDto);

        return new ResponseDto<>(requestDto, listDto , (int) musicPage.get().getTotalElements());
    }

    //===
    @Transactional
    @Override
    public void createSound(int checkedUserId,AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto){
        User checkedUser = userRepository.findById(2).orElseThrow();
        albumDto.setUserId(checkedUser.getUserId());
        musicDto.setUserId(checkedUser.getUserId());
        musicDto.setPrice(musicDto.getSoundType().equals("sfx") ? 2 : 3);
        Album album = albumRepository.save(albumDto.toEntity());
        Music music = musicRepository.save(musicDto.toEntity());
        albumMusicRepository.save(albumMusicToEntity(album,music,checkedUser));
        tagsServiceImpl.createSoundTags(music,tagsDto);
    }

    @Override
    public void updateAlbum(int albumId, AlbumDto albumDto) {
        Album modify = albumRepository.findById(albumId).orElseThrow();
        modify.update(albumDto.getAlbumName(), albumDto.getDescription());
    }

    @Override
    public void updateMusic(int musicId, MusicDto musicDto) {
        Music modify = musicRepository.findById(musicId).orElseThrow();
        modify.update(musicDto.getTitle(),musicDto.getDescription(), musicDto.getSoundType());
    }

    @Override
    public void deleteAlbum(int albumId) {
        albumRepository.deleteById(albumId);
    }

    @Override
    public void deleteMusic(int musicId) {
        musicRepository.deleteById(musicId);
    }
}

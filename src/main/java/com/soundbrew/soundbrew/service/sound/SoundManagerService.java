package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.mapper.AlbumMapper;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class SoundManagerService {
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumService albumService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private AlbumMusicService albumMusicService;
    @Autowired
    private TagsService tagsService;

    @Autowired
    private AlbumMapper albumMapper;

    @Transactional
    public void createSound(int checkedUserId,AlbumDto albumDto, MusicDto musicDto, InstrumentTagDto instrumentTagDto, MoodTagDto moodTagDto, GenreTagDto genreTagDto ){
        // return 받은 값을 통해서 추가적인 작업 하기

        //첫번째 방법 - Dto에 만들어둔 filtering 호출 - dto는 값 전달만 수행해야하는데, 변환까지 맡아서 역할이 애매해짐
//        albumDto.filterFields();
//        (...)

        // 두번째방법 - 서비스에서 직접 값 변환 - 값 변환 코드가 많아짐
//        albumDto.setAlbumName(filteringTrim(albumDto.getAlbumName()));
//        albumDto.setDescription(filteringTrim(albumDto.getDescription()));
//        (...)

        //추가적인 작업
        User ckeckedUser = userRepository.findById(2).orElseThrow();
        albumDto.setUserId(checkedUserId);
        musicDto.setUserId(checkedUserId);

        // 세번째 방법
//        Album album = albumService.createAlbum(albumMapper.toEntity(albumDto));
        Album album = albumService.createAlbum(albumDto);
        Music music = musicService.createMusic(musicDto);
        albumMusicService.createAlbumMusic(album,music,ckeckedUser);
        tagsService.createMusicInstrumentTag(music,instrumentTagDto);
        tagsService.createMusicMoodTag(music,moodTagDto);
        tagsService.createMusicGenreTag(music, genreTagDto);
    }

    @Transactional
    public void updateAlbum(int albumId, AlbumDto albumDto){
        Album modify = albumRepository.findById(albumId).orElseThrow();
        albumService.updateAlbum(modify,albumDto);
    }

    @Transactional
    public void updateMusic(int musicId, MusicDto musicDto ){
        // 인증 기능
        Music modify = musicRepository.findById(musicId).orElseThrow();
        musicService.updateMusic(modify,musicDto);
    }

    @Transactional
    public void updateMusicTags(int musicId, InstrumentTagDto instrumentTagDto, MoodTagDto moodTagDto, GenreTagDto genreTagDto) {
        // 인증 기능
        Music music = musicRepository.findById(musicId).orElseThrow();
        tagsService.updateMusicInstrumentTag(music,instrumentTagDto);
        tagsService.updateMusicMoodTag(music,moodTagDto);
        tagsService.updateMusicGenreTag(music,genreTagDto);
    }
}
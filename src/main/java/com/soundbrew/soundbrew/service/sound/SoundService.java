package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 음원(sound)에 대한 서비스 , 행위 중심 서비스, (album) - (album_music) - (music) - (music_tags) 까지 관여함
public interface SoundService {
    void createSound(int checkedUserId, AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto);
    // soundSearchRequestDto에 따른 음원 검색 (total)
    public SoundSearchFilterDto soundSearch(SoundSearchRequestDto soundSearchRequestDto, Pageable pageable);
    // nickName 아티스트의 앨범들 들고오기(아티스트의 다른 앨범 가져오기)
    public List<AlbumDto> readAlbumByArtistName(String nickName);
    // nickName 아티스트의 음원 보기
    public List<MusicDto> readMusicByArtistName(String nickName);

    // 어드민을 위해서 지금 findAll을 하는데, 특정 대상을 가져오는 기능도 생각해보자.
    // userId 회원의 수정할 앨범들 보기(album 만)
    public List<AlbumDto> readAlbum();
    // userId 회원의 수정할 음원 검색 (music 만)
    public List<MusicDto> readMusic();

    // albumId 앨범 수정
    public void updateAlbum(int albumId, AlbumDto albumDto);
    // musicId 음원 수정
    public void updateMusic(int musicId, MusicDto musicDto );

    // albumId 앨범 삭제
    public void deleteAlbum(int albumId);
    // musicId 음원 삭제
    public void deleteMusic(int musicId);
}

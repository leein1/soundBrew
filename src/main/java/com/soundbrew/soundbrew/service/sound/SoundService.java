package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

// 음원(sound)에 대한 서비스 , 행위 중심 서비스, (album) - (album_music) - (music) - (music_tags) 까지 관여함
public interface SoundService {

    // soundSearchRequestDto에 따른 음원 검색 (total, keyword, tags)
    ResponseDto<SearchTotalResultDto> totalSoundSearch(RequestDto requestDto);
    // soundSearchReqeustDto에 다른 앨범 검색 (total, keyword, tags)
    ResponseDto<SearchTotalResultDto> totalAlbumSearch(RequestDto searchRequestDto);
    // only album info (All *for admin*  / artist search *for admin* / my album *for artist*)
    ResponseDto<SearchTotalResultDto> getUsersAlbums(RequestDto requestDto); // all
    // only music info (All *for admin*  / artist search *for admin* / my music *for artist*)
    ResponseDto<SearchTotalResultDto> getUsersSounds(RequestDto requestDto); // all

    //생성
    void createSound(int checkedUserId, AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto);

    // albumId 앨범 수정
    void updateAlbum(int albumId, AlbumDto albumDto);
    // musicId 음원 수정
    void updateMusic(int musicId, MusicDto musicDto );

    // albumId 앨범 삭
    void deleteAlbum(int albumId);
    // musicId 음원 삭제
    void deleteMusic(int musicId);
}

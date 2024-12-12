package com.soundbrew.soundbrew.service.newservice;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

public interface MeService {
    // tags for me
    ResponseDto updateSoundTags(int musicId, TagsDto tagsDto);
    ResponseDto linkSoundTags(Music music, TagsDto tagsDto);
    // ResponseDto<TagsDto> tagsList(RequestDto requestDto); 전체, 회원 닉네임.

    // sounds for me
    ResponseDto createSound(int checkedUserId, AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto);
    ResponseDto updateAlbum(int albumId, AlbumDto albumDto);
    ResponseDto updateMusic(int musicId, MusicDto musicDto );

    ResponseDto<SearchResponseDto> getSoundsOne(String artist, int id); //음원 하나 보기
    ResponseDto<SearchAlbumResultDto> getAlbumOne(String artist, int id); //앨범 하나 보기( 앨범 정보만)
}

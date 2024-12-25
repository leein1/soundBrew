package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

import java.util.List;

public interface MeService {
    // tags for me
    ResponseDto updateLinkTags(int musicId, TagsDto tagsDto);
    ResponseDto linkTags(Music music, TagsDto tagsDto);
    // ResponseDto<TagsDto> tagsList(RequestDto requestDto); 전체, 회원 닉네임.

    // sounds for me
    ResponseDto createSound(int checkedUserId, AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto);
    ResponseDto updateAlbum(int albumId, AlbumDto albumDto);
    ResponseDto updateMusic(int musicId, MusicDto musicDto );

    // get one for me or admin
    ResponseDto<SearchTotalResultDto> getSoundOne(int userId, int id);
    ResponseDto<SearchTotalResultDto> getAlbumOne(int userId, int id, RequestDto requestDto);

    ResponseDto<SearchTotalResultDto> getSoundMe(RequestDto requestDto);
    ResponseDto<SearchTotalResultDto> getAlbumMe(RequestDto responseDto);
}

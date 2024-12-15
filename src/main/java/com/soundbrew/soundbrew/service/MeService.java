package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

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
    ResponseDto<SearchTotalResultDto> getSoundOne(String nickname, int id);
    ResponseDto<SearchTotalResultDto> getAlbumOne(String nickname, int id);
}

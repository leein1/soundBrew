package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;

public interface MeService {
    // tags for me
    ResponseDTO updateLinkTags(int musicId, TagsDTO tagsDTO);
    ResponseDTO linkTags(Music music, TagsDTO tagsDTO);
    // ResponseDTO<TagsDTO> tagsList(RequestDTO requestDto); 전체, 회원 닉네임.

    // sounds for me
    ResponseDTO createSound(int checkedUserId, AlbumDTO albumDTO, MusicDTO musicDTO, TagsDTO tagsDTO);
    ResponseDTO updateAlbum(int albumId, AlbumDTO albumDTO);
    ResponseDTO updateMusic(int musicId, MusicDTO musicDTO );

    // get one for me or admin
    ResponseDTO<SearchTotalResultDTO> getSoundOne(int userId, int id);
    ResponseDTO<SearchTotalResultDTO> getAlbumOne(int userId, int id, RequestDTO requestDTO);

    ResponseDTO<SearchTotalResultDTO> getSoundMe(RequestDTO requestDTO);
    ResponseDTO<SearchTotalResultDTO> getAlbumMe(RequestDTO requestDTO);
}

package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;

public interface SoundsService {
    // total search for all user
    ResponseDTO<SearchTotalResultDTO> totalSoundSearch(RequestDTO requestDTO); // 음원 목록 보기
    ResponseDTO<SearchTotalResultDTO> totalAlbumSearch(RequestDTO requestDTO); // 음원 목록 보기 - 앨범 view

    // get one for all user
    ResponseDTO<SearchTotalResultDTO> getSoundOne(String nickname, String title); // 음원 하나 보기
    ResponseDTO<SearchTotalResultDTO> getAlbumOne(String nickname, String albumName, RequestDTO requestDTO); // 앨범 하나 보기 ( 해당하는 음원은 여러개일수있음)

    // sounds for me
    ResponseDTO createSound(int checkedUserId, AlbumDTO albumDTO, MusicDTO musicDTO, TagsDTO tagsDTO);
    ResponseDTO updateAlbum(int albumId, AlbumDTO albumDTO);
    ResponseDTO updateMusic(int musicId, MusicDTO musicDTO );

    // get one for me or admin
    ResponseDTO<SearchTotalResultDTO> getSoundOne(int userId, int id);
    ResponseDTO<SearchTotalResultDTO> getAlbumOne(int userId, int id, RequestDTO requestDTO);
    ResponseDTO<SearchTotalResultDTO> getSoundMe(RequestDTO requestDTO);
    ResponseDTO<SearchTotalResultDTO> getAlbumMe(RequestDTO requestDTO);

    ResponseDTO deleteAlbum(int albumId);
    ResponseDTO deleteMusic(int musicId);

    ResponseDTO updateVerifyAlbum(int albumId);
    ResponseDTO<SearchTotalResultDTO> readVerifyAlbum(RequestDTO requestDTO);
    ResponseDTO<SearchTotalResultDTO> readVerifyAlbumOne(int userId, int id, RequestDTO requestDTO);

    ResponseDTO<SearchTotalResultDTO> getSoundOneForAdmin(int userId, int id);
    ResponseDTO<SearchTotalResultDTO> getAlbumOneForAdmin(int userId, int id,RequestDTO requestDTO);


}

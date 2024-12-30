package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.dto.sound.*;

import java.util.List;

public interface AdminService {

    public ResponseDTO<UserDetailsDTO> getAlluser(RequestDTO requestDTO);

    public ResponseDTO<UserDetailsDTO> getUser(int userId);

//    // Tags for admin
//    ResponseDTO createTag(TagsDTO tagsDTO);
//    ResponseDTO updateInstrumentTagSpelling(String beforeName, String afterName);
//    ResponseDTO updateMoodTagSpelling(String beforeName, String afterName);
//    ResponseDTO updateGenreTagSpelling(String beforeName, String afterName);
//    ResponseDTO<TagsDTO> getTags(List<Integer> musicIds);
//
//    ResponseDTO deleteAlbum(int albumId);
//    ResponseDTO deleteMusic(int musicId);
//
//    ResponseDTO updateVerifyAlbum(int albumId);
//    ResponseDTO<SearchTotalResultDTO> readVerifyAlbum(RequestDTO requestDTO);
//    ResponseDTO<SearchTotalResultDTO> readVerifyAlbumOne(int userId, int id, RequestDTO requestDTO);
}

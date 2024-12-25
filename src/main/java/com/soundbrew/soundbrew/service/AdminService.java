package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;

import java.util.List;

public interface AdminService {
    // Tags for admin
    ResponseDTO createTag(TagsDTO tagsDTO);
    ResponseDTO updateInstrumentTagSpelling(String beforeName, String afterName);
    ResponseDTO updateMoodTagSpelling(String beforeName, String afterName);
    ResponseDTO updateGenreTagSpelling(String beforeName, String afterName);
    ResponseDTO<TagsDTO> getTags(List<Integer> musicIds);

    ResponseDTO deleteAlbum(int albumId);
    ResponseDTO deleteMusic(int musicId);
}

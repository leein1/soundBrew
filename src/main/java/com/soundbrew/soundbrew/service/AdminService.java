package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

import java.util.List;

public interface AdminService {
    // Tags for admin
    ResponseDto createTag(TagsDto tagsDto);
    ResponseDto updateInstrumentTagSpelling(String beforeName, String afterName);
    ResponseDto updateMoodTagSpelling(String beforeName, String afterName);
    ResponseDto updateGenreTagSpelling(String beforeName, String afterName);
    ResponseDto<TagsDto> getTags(List<Integer> musicIds);

    ResponseDto deleteAlbum(int albumId);
    ResponseDto deleteMusic(int musicId);
}

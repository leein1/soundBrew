package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

import java.util.List;

public interface AdminService {
    // Tags for admin
    ResponseDto createTag(TagsDto tagsDto);
    ResponseDto updateInstrumentTagSpelling(String beforeName, String afterName);
    ResponseDto updateMoodTagSpelling(String beforeName, String afterName);
    ResponseDto updateGenreTagSpelling(String beforeName, String afterName);
    ResponseDto updateLinkTags(int musicId, TagsDto tagsDto);
    ResponseDto linkTags(Music music, TagsDto tagsDto);
    ResponseDto<TagsDto> getTags(List<Integer> musicIds);

    // sounds for admin
    ResponseDto deleteAlbum(int albumId);
    ResponseDto deleteMusic(int musicId);
    ResponseDto updateAlbum(int albumId, AlbumDto albumDto);
    ResponseDto updateMusic(int musicId, MusicDto musicDto );
    // get one for me or admin
    ResponseDto<SearchTotalResultDto> getSoundOne(String nickname, int id);
    ResponseDto<SearchTotalResultDto> getAlbumOne(String nickname, int id);
}

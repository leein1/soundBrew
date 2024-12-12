package com.soundbrew.soundbrew.service.newservice;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;

import java.util.List;

public interface AdminService {
    // Tags for admin
    ResponseDto createTag(TagsDto tagsDto);
    ResponseDto updateInstrumentTagSpelling(String beforeName, String afterName);
    ResponseDto updateMoodTagSpelling(String beforeName, String afterName);
    ResponseDto updateGenreTagSpelling(String beforeName, String afterName);
    ResponseDto updateSoundTags(int musicId, TagsDto tagsDto);
    ResponseDto linkSoundTags(Music music, TagsDto tagsDto);
    ResponseDto<TagsDto> getTags(List<Integer> musicIds);

    // sounds for admin
    ResponseDto deleteAlbum(int albumId);
    ResponseDto deleteMusic(int musicId);
    ResponseDto updateAlbum(int albumId, AlbumDto albumDto);
    ResponseDto updateMusic(int musicId, MusicDto musicDto );

    ResponseDto<SearchResponseDto> getSoundsOne(String artist, int id); //음원 하나 보기
    ResponseDto<SearchAlbumResultDto> getAlbumOne(String artist, int id); //앨범 하나 보기( 앨범 정보만)
}

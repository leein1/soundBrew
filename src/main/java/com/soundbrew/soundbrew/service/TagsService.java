package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.dto.sound.TagsDTO;

import java.util.List;

public interface TagsService {

    //일단 getAllTags를 쓰는 식으로 변경되어서 일단 사용은 안함.
    ResponseDTO<TagsDTO> tagsSearchAfter(List<SearchTotalResultDTO> sounds); // 음원 목록에 해당하는 태그들 버튼화 해서 들고오기 ( 즉 음원 목록을 변수로 받음)

    ResponseDTO updateLinkTagsForAdmin(int musicId, TagsDTO tagsDTO);
    ResponseDTO updateLinkTagsForArtist(int musicId, TagsDTO tagsDTO,int userId);
    ResponseDTO linkTags(Music music, TagsDTO tagsDTO);

    // Tags for admin
    ResponseDTO createTag(TagsDTO tagsDTO);
    ResponseDTO updateInstrumentTagSpelling(String beforeName, String afterName);
    ResponseDTO updateMoodTagSpelling(String beforeName, String afterName);
    ResponseDTO updateGenreTagSpelling(String beforeName, String afterName);
    ResponseDTO<TagsDTO> getTags();
    ResponseDTO<TagsDTO> getAllTags(RequestDTO requestDTO);
}

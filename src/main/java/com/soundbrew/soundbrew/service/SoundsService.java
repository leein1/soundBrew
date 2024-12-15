package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import org.springframework.http.HttpRange;

import java.io.IOException;
import java.util.List;

public interface SoundsService {
    // streaming for all user
    ResponseDto<SoundStreamDto> streamSound(HttpRange range, String fileName) throws IOException;

    // total search for all user
    ResponseDto<SearchTotalResultDto> totalSoundSearch(RequestDto requestDto); // 음원 목록 보기
    ResponseDto<SearchTotalResultDto> totalAlbumSearch(RequestDto requestDto); // 음원 목록 보기 - 앨범 view
    ResponseDto<TagsDto> totalTagsSearch(List<SearchTotalResultDto> sounds); // 음원 목록에 해당하는 태그들 버튼화 해서 들고오기

    // get one for all user
    ResponseDto<SearchTotalResultDto>   getSoundOne(String nickname, String title); // 음원 하나 보기
    ResponseDto<SearchTotalResultDto> getAlbumOne(String nickname, String albumName, RequestDto requestDto); // 앨범 하나 보기 ( 해당하는 음원은 여러개일수있음)

}

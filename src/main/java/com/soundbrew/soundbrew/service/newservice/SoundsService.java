package com.soundbrew.soundbrew.service.newservice;

import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import org.springframework.http.HttpRange;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SoundsService {
    // streaming for all user
    ResponseDto<SoundStreamDto> streamSound(HttpRange range, String fileName) throws IOException;

    // total search for all user
    ResponseDto<SearchTotalResultDto> totalSoundSearch(RequestDto requestDto); // 음원 목록 보기
    ResponseDto<SearchTotalResultDto> totalAlbumSearch(RequestDto requestDto); // 음원 목록 보기 - 앨범 view
    ResponseDto<TagsDto> totalTagsSearch(List<SearchTotalResultDto> sounds); // 음원 목록에 해당하는 태그들 버튼화 해서 들고오기

    // get one for all user
    ResponseDto<SearchTotalResultDto> getSoundsOne(String nickname, String title); // 음원 하나 보기
    ResponseDto<SearchTotalResultDto> getAlbumsOne(String nickname, String albumName, RequestDto requestDto); // 앨범 하나 보기 ( 해당하는 음원은 여러개일수있음)

    // get one for me or admin
    ResponseDto<SearchResponseDto> getSoundsOne(String artist, int id); //음원 하나 보기
    ResponseDto<SearchAlbumResultDto> getAlbumOne(String artist, int id); //앨범 하나 보기( 앨범 정보만)
}

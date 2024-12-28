package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import org.springframework.http.HttpRange;

import java.io.IOException;
import java.util.List;

public interface SoundsService {
    // streaming for all user
    ResponseDTO<SoundStreamDTO> streamSound(HttpRange range, String fileName) throws IOException;

    // total search for all user
    ResponseDTO<SearchTotalResultDTO> totalSoundSearch(RequestDTO requestDTO); // 음원 목록 보기
    ResponseDTO<SearchTotalResultDTO> totalAlbumSearch(RequestDTO requestDTO); // 음원 목록 보기 - 앨범 view
    ResponseDTO<TagsDTO> totalTagsSearch(List<SearchTotalResultDTO> sounds); // 음원 목록에 해당하는 태그들 버튼화 해서 들고오기

    // get one for all user
    ResponseDTO<SearchTotalResultDTO> getSoundOne(String nickname, String title); // 음원 하나 보기
    ResponseDTO<SearchTotalResultDTO> getAlbumOne(String nickname, String albumName, RequestDTO requestDTO); // 앨범 하나 보기 ( 해당하는 음원은 여러개일수있음)

}

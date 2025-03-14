package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundMyStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundTotalStatisticDTO;

import java.util.List;

public interface SoundsStatisticService {
    ResponseDTO<SoundTotalStatisticDTO> getSoundStats(RequestDTO requestDTO);
    ResponseDTO<SoundStatisticDTO> getBestSellingTrack(RequestDTO requestDTO) ;
    ResponseDTO<SoundStatisticDTO> getBestSellingArtist(RequestDTO requestDTO);
    ResponseDTO<SoundStatisticDTO> getBestUploadArtist(RequestDTO requestDTO);

    //나의 통계
    ResponseDTO<SoundMyStatisticDTO> getMySoundStats(int userId);
}

package com.soundbrew.soundbrew.dto.statistics.sound;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SoundTotalStatisticDTO {
    private int totalAlbums; // 총 앨범 수
    private int totalMusics; // 총 음원 수
    private List<SoundStatisticDTO> topSellingMusic; // 가장 많이 판매된 5 음원 (다운로드 기준)
    private List<SoundStatisticDTO> topArtistsBySales; // 가장 많은 곡을 판매한 아티스트 5명
    private List<SoundStatisticDTO> topArtistsByUploads; // 가장 많은 곡을 등록한 아티스트 5명
}

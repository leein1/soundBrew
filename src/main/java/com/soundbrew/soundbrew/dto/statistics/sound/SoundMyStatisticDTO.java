package com.soundbrew.soundbrew.dto.statistics.sound;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoundMyStatisticDTO {
    // 내가 올린 음원 개수 (기간별)
    private int musicCountDay;
    private int musicCountWeek;
    private int musicCountMonth;

    private int musicCountTotal;
    private int albumCountTotal;

    // 다운로드 횟수 (기간별)
    private int downloadsDay;
    private int downloadsWeek;
    private int downloadsMonth;

    // 전체 다운로드 횟수
    private int totalDownloads;

    // 가장 많이 다운로드된 음원 정보
    private List<Integer> topTrackIds;
    private List<String> topTrackTitles;
    private List<Integer> topTrackDownloadCounts;


}


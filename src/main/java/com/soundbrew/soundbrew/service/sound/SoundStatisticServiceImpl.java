package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundMyStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundTotalStatisticDTO;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoundStatisticServiceImpl implements SoundsStatisticService {
    private final AlbumRepository albumRepository;
    private final MusicRepository musicRepository;

    @Override
    public ResponseDTO<SoundTotalStatisticDTO> getSoundStats(RequestDTO requestDTO) {
        // 전체 앨범 수와 음원 수
        int totalAlbums = (int) albumRepository.count();
        int totalMusics = (int) musicRepository.count();

        // 상위 5개 음원 (다운로드 기준)
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Object[]> topSellingMusicData = musicRepository.findTopSellingTracks(pageRequest);
        List<SoundStatisticDTO> topSellingMusic = topSellingMusicData.stream()
                .map(row -> SoundStatisticDTO.builder()
                        .musicId(((Number) row[0]).longValue())
                        .title((String) row[1])
                        .count(((Number) row[2]).longValue())
                        .nickname((String) row[3])
                        .build())
                .toList();

        // 상위 5 아티스트 (판매 기준)
        List<Object[]> topArtistsBySalesData = musicRepository.findArtistWithMostSales(pageRequest);
        List<SoundStatisticDTO> topArtistsBySales = topArtistsBySalesData.stream()
                .map(row -> SoundStatisticDTO.builder()
                        .userId(((Number) row[0]).longValue())
                        .count(((Number) row[1]).longValue())
                        .nickname((String) row[2])
                        .build())
                .toList();

        // 상위 5 아티스트 (업로드 기준)
        List<Object[]> topArtistsByUploadsData = musicRepository.findArtistWithMostUploadedMusic(pageRequest);
        List<SoundStatisticDTO> topArtistsByUploads = topArtistsByUploadsData.stream()
                .map(row -> SoundStatisticDTO.builder()
                        .userId(((Number) row[0]).longValue())
                        .count(((Number) row[1]).longValue())
                        .nickname((String) row[2])
                        .build())
                .toList();

        // 모든 통계를 하나의 DTO에 담음
        SoundTotalStatisticDTO totalStatisticDTO = SoundTotalStatisticDTO.builder()
                .totalAlbums(totalAlbums)
                .totalMusics(totalMusics)
                .topSellingMusic(topSellingMusic)
                .topArtistsBySales(topArtistsBySales)
                .topArtistsByUploads(topArtistsByUploads)
                .build();

        // 단일 객체이므로 리스트에 감싸서 반환 (withAll 메서드가 List와 total 값을 요구하는 경우)
        return ResponseDTO.<SoundTotalStatisticDTO>withSingleData().dto(totalStatisticDTO).build();
    }

    @Override
    public ResponseDTO<SoundStatisticDTO> getBestSellingTrack(RequestDTO requestDTO) {
        PageRequest pageRequest = PageRequest.of(0, 5);

        List<Object[]> topArtistsBySalesData = musicRepository.findTopSellingTracks(pageRequest);
        List<SoundStatisticDTO> topTracksBySales = topArtistsBySalesData.stream()
                .map(row -> SoundStatisticDTO.builder()
                        .musicId(((Number) row[0]).longValue())  // ID 변환
                        .title((String) row[1])                 // 제목 변환
                        .count(((Number) row[2]).longValue())   // 다운로드 수 변환
                        .nickname((String) row[3])
                        .build())
                .toList();

        return ResponseDTO.withAll(requestDTO, topTracksBySales, topTracksBySales.size());
    }

    @Override
    public ResponseDTO<SoundStatisticDTO> getBestSellingArtist(RequestDTO requestDTO) {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Object[]> result = musicRepository.findArtistWithMostSales(pageRequest);
        List<SoundStatisticDTO> topArtistsBySales = result.stream()
                .map(row -> SoundStatisticDTO.builder()
                        .userId((long) row[0])
                        .count((long) row[1])
                        .nickname((String) row[2])
                        .build())
                .toList();

        return ResponseDTO.withAll(requestDTO,topArtistsBySales,result.size());
    }

    @Override
    public ResponseDTO<SoundStatisticDTO> getBestUploadArtist(RequestDTO requestDTO) {
        PageRequest pageRequest = PageRequest.of(0, 5);

        List<Object[]> result = musicRepository.findArtistWithMostUploadedMusic(pageRequest);
        List<SoundStatisticDTO> bestUpload = result.stream()
                .map(row -> SoundStatisticDTO.builder()
                        .userId((long) row[0])
                        .count((long) row[1])
                        .nickname((String) row[2])
                        .build())
                .toList();

        return  ResponseDTO.withAll(requestDTO, bestUpload, bestUpload.size());
    }

    @Override
    public ResponseDTO<SoundMyStatisticDTO> getMySoundStats(int userId) {
        PageRequest pageRequest = PageRequest.of(0, 5);

        // 기준 시간 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        // 1. 내가 올린 음원 개수 (기간별)
        int musicCountDay = musicRepository.countUserMusicSince(userId, oneDayAgo);
        int musicCountWeek = musicRepository.countUserMusicSince(userId, sevenDaysAgo);
        int musicCountMonth = musicRepository.countUserMusicSince(userId, thirtyDaysAgo);
        // 전체 음원 개수와 전체 앨범 개수 (JPA 내장 메서드 사용)
        int musicCountTotal = musicRepository.countByUserId(userId);

        int albumCountTotal = albumRepository.countByUserId(userId);

        // 2. 음원 다운로드 횟수 (기간별)
        int downloadsDay = musicRepository.sumUserDownloadsSince(userId, oneDayAgo);
        int downloadsWeek = musicRepository.sumUserDownloadsSince(userId, sevenDaysAgo);
        int downloadsMonth = musicRepository.sumUserDownloadsSince(userId, thirtyDaysAgo);
        // 3. 전체 다운로드 횟수 (기간 제한 없음)
        int totalDownloads = musicRepository.sumUserDownloadsTotal(userId);

        // 4. 가장 많이 다운로드된 음원 조회 (최상위 1개)
        List<Object[]> topTrackListData = musicRepository.findTopSellingTracksByUser(userId, pageRequest);
        List<Integer> topTrackIds = new ArrayList<>();
        List<String> topTrackTitles = new ArrayList<>();
        List<Integer> topTrackDownloadCounts = new ArrayList<>();

        if (topTrackListData != null && !topTrackListData.isEmpty()) {
            for (Object[] row : topTrackListData) {
                topTrackIds.add(((Number) row[0]).intValue());
                topTrackTitles.add((String) row[1]);
                topTrackDownloadCounts.add(((Number) row[2]).intValue());
            }
        }

        // DTO 빌더로 결과 조합
        SoundMyStatisticDTO dto = SoundMyStatisticDTO.builder()
                .musicCountDay(musicCountDay)
                .musicCountWeek(musicCountWeek)
                .musicCountMonth(musicCountMonth)
                .downloadsDay(downloadsDay)
                .downloadsWeek(downloadsWeek)
                .downloadsMonth(downloadsMonth)
                .totalDownloads(totalDownloads)
                .topTrackIds(topTrackIds)
                .topTrackTitles(topTrackTitles)
                .topTrackDownloadCounts(topTrackDownloadCounts)
                .musicCountTotal(musicCountTotal)
                .albumCountTotal(albumCountTotal)
                .build();

        return ResponseDTO.<SoundMyStatisticDTO>withSingleData().dto(dto).build();
    }

}

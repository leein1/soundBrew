package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundTotalStatisticDTO;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
}

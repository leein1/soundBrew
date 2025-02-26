package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundStatisticDTO;
import com.soundbrew.soundbrew.repository.sound.custom.MusicRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Integer>, MusicRepositoryCustom {
    Optional<List<Music>> findByUserId(int userId);

    // 11. 가장 많이 판매된 음원 (salesCount 기준 내림차순)
    @Query("""
        SELECT m.musicId, m.title, m.download, m.nickname
        FROM Music m
        ORDER BY m.download DESC
    """)
    List<Object[]> findTopSellingTracks(Pageable pageable);


    // 12. 가장 많은 곡을 판매한 아티스트
    // 음원별 판매 건수(salesCount)를 합산하여, 아티스트(userId)별 총 판매 건수를 내림차순 정렬
    @Query("""
        SELECT m.userId, SUM(m.download), MIN(m.nickname)
        FROM Music m
        GROUP BY m.userId
        ORDER BY SUM(m.download) DESC
    """)
    List<Object[]> findArtistWithMostSales(Pageable pageable);


    // 13. 가장 많이 곡을 등록한 아티스트
    // 단순히 음원 등록 건수를 세어, 아티스트(userId)별 음원 개수를 내림차순 정렬
    @Query("""
        SELECT m.userId, COUNT(m), MIN(m.nickname)
        FROM Music m
        GROUP BY m.userId
        ORDER BY COUNT(m) DESC
    """)
    List<Object[]> findArtistWithMostUploadedMusic(Pageable pageable);

    //me

    // 0. 내가 올린 음원 중 가장 다운로드 많이 된 음원
    @Query("""
        SELECT m.musicId, m.title, m.download, m.nickname
        FROM Music m
        WHERE m.userId = :userId
        ORDER BY m.download DESC
    """)
    List<Object[]> findTopSellingTracksByUser(@Param("userId") int userId, Pageable pageable);

    // 1. 내가 올린 음원 개수를 조회 (예: 최근 기준일 이후)
    @Query("SELECT COUNT(m) FROM Music m WHERE m.userId = :userId AND m.createDate >= :startDate")
    int countUserMusicSince(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate);

    // 2. 내가 올린 음원의 다운로드 총합 (예: 특정 기간 내)
    @Query("SELECT COALESCE(SUM(m.download), 0) FROM Music m WHERE m.userId = :userId AND m.createDate >= :startDate")
    int sumUserDownloadsSince(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate);

    // 3. 전체 음원 다운로드 합계 (기간 제한 없이)
    @Query("SELECT COALESCE(SUM(m.download), 0) FROM Music m WHERE m.userId = :userId")
    int sumUserDownloadsTotal(@Param("userId") int userId);

    int countByUserId(int userId);

}

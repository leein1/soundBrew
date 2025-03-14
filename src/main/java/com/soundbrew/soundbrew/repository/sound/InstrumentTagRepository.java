package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import com.soundbrew.soundbrew.dto.statistics.tag.TagStatisticDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface InstrumentTagRepository extends JpaRepository<InstrumentTag, Integer> {
    Optional<InstrumentTag> findByInstrumentTagName(String tagName);


    //대시보드 관련 기능 추가
    @Query("""
        SELECT i.instrumentTagName, COUNT(mi)
        FROM InstrumentTag i
        LEFT JOIN i.musicInstrumentTag mi
        GROUP BY i.instrumentTagId, i.instrumentTagName
        ORDER BY COUNT(mi) DESC
    """)
    List<Object[]> findTopInstrumentTags(Pageable pageable);

    // +특정 대상(나)
    @Query("""
        SELECT i.instrumentTagName, COUNT(mi)
        FROM InstrumentTag i
        LEFT JOIN i.musicInstrumentTag mi
        LEFT JOIN mi.music m
        WHERE (m.user.userId = :userId)
        GROUP BY i.instrumentTagId, i.instrumentTagName
        ORDER BY COUNT(mi) DESC
    """)
    List<Object[]> findTopInstrumentTagsByUser(@Param("userId") int userId, Pageable pageable);

}

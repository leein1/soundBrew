package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MoodTag;
import com.soundbrew.soundbrew.dto.statistics.tag.TagStatisticDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoodTagRepository extends JpaRepository<MoodTag, Integer> {
    Optional<MoodTag> findByMoodTagName(String tagName);

    //대시보드 관련 기능 추가
    @Query("""
        SELECT i.moodTagName, COUNT(mi)
        FROM MoodTag i
        LEFT JOIN i.musicMoodTag mi
        GROUP BY i.moodTagId, i.moodTagName
        ORDER BY COUNT(mi) DESC
    """)
    List<Object[]> findTopMoodTags(Pageable pageable);
}

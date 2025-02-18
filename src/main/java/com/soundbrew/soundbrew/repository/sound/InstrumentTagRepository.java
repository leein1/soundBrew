package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface InstrumentTagRepository extends JpaRepository<InstrumentTag, Integer> {
    Optional<InstrumentTag> findByInstrumentTagName(String tagName);


    //대시보드 관련 기능 추가

    /*
    [
        ["Piano", 100],
        ["Violin", 80],
        ["Guitar", 60]
    ]

    이렇게 응답이 나오는 관계로, Object[] 형을 사용했다. 즉, ORDER BY COUNT(mi) DESC 를 통해 순서대로, instrumentTagName 를 통해서
    "악기명", "카운트"가 나온다
    */
    @Query("""
        SELECT i.instrumentTagName, COUNT(mi)
        FROM InstrumentTag i
        LEFT JOIN i.musicInstrumentTag mi
        GROUP BY i.instrumentTagId, i.instrumentTagName
        ORDER BY COUNT(mi) DESC
    """)
    List<Object[]> findTopInstrumentTags(Pageable pageable);
}

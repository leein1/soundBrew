package com.soundbrew.soundbrew.repository.sound;
import com.soundbrew.soundbrew.dto.statistics.tag.TagStatisticDTO;
import org.springframework.data.domain.Pageable;
import com.soundbrew.soundbrew.domain.sound.GenreTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GenreTagRepository extends JpaRepository<GenreTag, Integer> {
    Optional<GenreTag> findByGenreTagName(String tagName);

    //대시보드 관련 기능 추가
    @Query("""
        SELECT i.genreTagName, COUNT(mi)
        FROM GenreTag i
        LEFT JOIN i.musicGenreTag mi
        GROUP BY i.genreTagId, i.genreTagName
        ORDER BY COUNT(mi) DESC
    """)
    List<Object[]> findTopGenreTags(Pageable pageable);

    // + 나
    @Query("""
        SELECT i.genreTagName, COUNT(mi)
        FROM GenreTag i
        LEFT JOIN i.musicGenreTag mi
        LEFT JOIN mi.music m
        WHERE m.user.userId = :userId
        GROUP BY i.genreTagId, i.genreTagName
        ORDER BY COUNT(mi) DESC
    """)
    List<Object[]> findTopGenreTagsByUser(@Param("userId") int userId, Pageable pageable);

}

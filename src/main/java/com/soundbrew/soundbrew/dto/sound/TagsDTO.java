package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.sound.GenreTag;
import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MoodTag;
import com.soundbrew.soundbrew.util.valid.ValidTagList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TagsDTO {
    private Integer musicId;
    private String title;

    @ValidTagList
    private List<String> instrument;
    @ValidTagList
    private List<String> mood;
    @ValidTagList
    private List<String> genre;

    // 대시보드 관련 기능 추가
    private Map<String, Long> instrumentUsageCount; // instrument 태그별 사용 횟수
    private Map<String, Long> moodUsageCount; // mood 태그별 사용 횟수
    private Map<String, Long> genreUsageCount; // genre 태그별 사용 횟수

    public void setTagUsageCounts(Map<String, Long> instrumentCounts, Map<String, Long> moodCounts, Map<String, Long> genreCounts) {
        this.instrumentUsageCount = instrumentCounts;
        this.moodUsageCount = moodCounts;
        this.genreUsageCount = genreCounts;
    }
    // =====

    public List<InstrumentTag> InstToEntity(){
        return instrument.stream()
                .map(tagName -> InstrumentTag.builder()
                        .instrumentTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }

    public List<MoodTag> moodToEntity(){
        return mood.stream()
                .map(tagName -> MoodTag.builder()
                        .moodTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }

    public List<GenreTag> genreToEntity(){
        return genre.stream()
                .map(tagName -> GenreTag.builder()
                        .genreTagName(tagName)
                        .build())
                .collect(Collectors.toList());
    }

}

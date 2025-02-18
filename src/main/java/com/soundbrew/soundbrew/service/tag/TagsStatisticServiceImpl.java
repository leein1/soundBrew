package com.soundbrew.soundbrew.service.tag;

import com.soundbrew.soundbrew.dto.sound.TagsDTO;
import com.soundbrew.soundbrew.repository.sound.GenreTagRepository;
import com.soundbrew.soundbrew.repository.sound.InstrumentTagRepository;
import com.soundbrew.soundbrew.repository.sound.MoodTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsStatisticServiceImpl implements  TagsStatisticService {
    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;

    @Override
    public TagsDTO getTagsWithTopUsage() {
        // 1. 사용 횟수 조회
        Map<String, Long> instrumentCounts = getTagUsageCounts(instrumentTagRepository.findTopInstrumentTags(PageRequest.of(0, 5)));
        Map<String, Long> moodCounts = getTagUsageCounts(moodTagRepository.findTopMoodTags(PageRequest.of(0, 5)));
        Map<String, Long> genreCounts = getTagUsageCounts(genreTagRepository.findTopGenreTags(PageRequest.of(0, 5)));

        // 2. TagsDTO 생성 및 사용 횟수 설정
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setTagUsageCounts(instrumentCounts, moodCounts, genreCounts);

        return tagsDTO;
    }

    private Map<String, Long> getTagUsageCounts(List<Object[]> tagUsageList) {
        return tagUsageList.stream()
                .collect(Collectors.toMap(
                        tagUsage -> (String) tagUsage[0],  // 태그 이름
                        tagUsage -> (Long) tagUsage[1],      // 사용 횟수
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new                 // 순서를 유지하는 Map 사용
                ));
    }
}

package com.soundbrew.soundbrew.service.tag;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.tag.TagStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.tag.TagsTotalStatisticDTO;
import com.soundbrew.soundbrew.repository.sound.GenreTagRepository;
import com.soundbrew.soundbrew.repository.sound.InstrumentTagRepository;
import com.soundbrew.soundbrew.repository.sound.MoodTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsStatisticServiceImpl implements TagsStatisticService {

    private final InstrumentTagRepository instrumentTagRepository;
    private final MoodTagRepository moodTagRepository;
    private final GenreTagRepository genreTagRepository;

    @Override
    public ResponseDTO<TagsTotalStatisticDTO> getTagsWithTopUsage() {
        // Object[]로 조회
        List<Object[]> instObjects = instrumentTagRepository.findTopInstrumentTags(PageRequest.of(0, 5));
        List<Object[]> moodObjects = moodTagRepository.findTopMoodTags(PageRequest.of(0, 5));
        List<Object[]> genreObjects = genreTagRepository.findTopGenreTags(PageRequest.of(0, 5));

        // Object[] 배열을 TagStatisticDTO 리스트로 변환 (첫번째 요소: tagName, 두번째 요소: count)
        List<TagStatisticDTO> instStatisticDTO = instObjects.stream()
                .map(obj -> new TagStatisticDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .collect(Collectors.toList());

        List<TagStatisticDTO> moodStatisticDTO = moodObjects.stream()
                .map(obj -> new TagStatisticDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .collect(Collectors.toList());

        List<TagStatisticDTO> genreStatisticDTO = genreObjects.stream()
                .map(obj -> new TagStatisticDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .collect(Collectors.toList());

        // DTO에 변환한 결과 설정
        TagsTotalStatisticDTO statisticDTO = new TagsTotalStatisticDTO();
        statisticDTO.setInstrumentUsageCount(instStatisticDTO);
        statisticDTO.setMoodUsageCount(moodStatisticDTO);
        statisticDTO.setGenreUsageCount(genreStatisticDTO);

        return ResponseDTO.<TagsTotalStatisticDTO>withSingleData().dto(statisticDTO).build();
    }
}

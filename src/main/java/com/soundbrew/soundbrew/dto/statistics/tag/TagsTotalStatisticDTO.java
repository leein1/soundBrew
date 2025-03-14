package com.soundbrew.soundbrew.dto.statistics.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagsTotalStatisticDTO {
    private List<TagStatisticDTO> instrumentUsageCount;
    private List<TagStatisticDTO> moodUsageCount;
    private List<TagStatisticDTO> genreUsageCount;
}

package com.soundbrew.soundbrew.service.tag;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.tag.TagsTotalStatisticDTO;

public interface TagsStatisticService {
    ResponseDTO<TagsTotalStatisticDTO> getTagsWithTopUsage();
    ResponseDTO<TagsTotalStatisticDTO> getTagsWithTopUsageByUserId(int userId);
}

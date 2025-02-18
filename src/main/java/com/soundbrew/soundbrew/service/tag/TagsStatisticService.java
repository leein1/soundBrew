package com.soundbrew.soundbrew.service.tag;

import com.soundbrew.soundbrew.dto.sound.TagsDTO;

import java.util.Map;

public interface TagsStatisticService {
    TagsDTO getTagsWithTopUsage();
}

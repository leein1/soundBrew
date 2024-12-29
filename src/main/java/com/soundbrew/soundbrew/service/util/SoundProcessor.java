package com.soundbrew.soundbrew.service.util;

import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SoundProcessor extends StringProcessorImpl{
    //  태그를 set에 저장
    public void addTagsToSet(String tagNames, Set<String> tagSet) {
        if (tagNames != null) {
            String[] tags = tagNames.split("[, ]+");
            for (String tag : tags) {
                if (!tag.isEmpty()) {
                    tagSet.add(tag.trim());
                }
            }
        }
    }
}

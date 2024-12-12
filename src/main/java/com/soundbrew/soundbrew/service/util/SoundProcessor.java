package com.soundbrew.soundbrew.service.util;

import com.soundbrew.soundbrew.dto.sound.SearchResponseDto;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SoundProcessor extends StringProcessorImpl{

    // ","로 이어진 태그문자열을 " " 로 바꾸어 프론트에서 예쁘게 보이기 위한 재단
    public List<SearchTotalResultDto> replaceCommaWithSpace(List<SearchTotalResultDto> sounds) {
        for (SearchTotalResultDto sound : sounds) {
            if(sound.getInstrumentTagName()!=null){
                String replaceInstTags = sound.getInstrumentTagName().replace(",", " ");
                sound.setInstrumentTagName(replaceInstTags);
            }
            if(sound.getMoodTagName()!=null){
                String replaceMoodTags = sound.getMoodTagName().replace("," , " ");
                sound.setMoodTagName(replaceMoodTags);
            }
            if(sound.getGenreTagName()!=null){
                String replaceGenreTags = sound.getGenreTagName().replace(","," ");
                sound.setGenreTagName(replaceGenreTags);
            }
        }
        return sounds;
    }

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

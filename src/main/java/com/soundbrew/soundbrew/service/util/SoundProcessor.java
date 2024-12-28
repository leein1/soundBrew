package com.soundbrew.soundbrew.service.util;

import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SoundProcessor extends StringProcessorImpl{

    // ","로 이어진 태그문자열을 " " 로 바꾸어 프론트에서 예쁘게 보이기 위한 재단
    public List<SearchTotalResultDTO> replaceCommaWithSpace(List<SearchTotalResultDTO> sounds) {
        for (SearchTotalResultDTO sound : sounds) {
            if(sound.getTagsStreamDTO().getInstrumentTagName()!=null){
                String replaceInstTags = sound.getTagsStreamDTO().getInstrumentTagName().replace(",", " ");
                sound.getTagsStreamDTO().setInstrumentTagName(replaceInstTags);
            }
            if(sound.getTagsStreamDTO().getMoodTagName()!=null){
                String replaceMoodTags = sound.getTagsStreamDTO().getMoodTagName().replace("," , " ");
                sound.getTagsStreamDTO().setMoodTagName(replaceMoodTags);
            }
            if(sound.getTagsStreamDTO().getGenreTagName()!=null){
                String replaceGenreTags = sound.getTagsStreamDTO().getGenreTagName().replace(","," ");
                sound.getTagsStreamDTO().setGenreTagName(replaceGenreTags);
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

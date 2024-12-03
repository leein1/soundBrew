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

    //  태그를 분리하고 중복 제거
    public SearchResponseDto replaceTagsToArray(List<SearchTotalResultDto> sounds) {
        SearchResponseDto afterTags = new SearchResponseDto();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SearchTotalResultDto sound : sounds) {
            // Instrument tags 처리
            if (sound.getInstrumentTagName() != null) {
                // Split by space as well as commas, to ensure tags are separated correctly
                String[] instTags = sound.getInstrumentTagName().split("[, ]+");
                for (String tag : instTags) {
                    if (!tag.isEmpty()) {
                        instTagSet.add(tag.trim()); // Add only non-empty trimmed tags
                    }
                }
            }

            // Mood tags 처리
            if (sound.getMoodTagName() != null) {
                String[] moodTags = sound.getMoodTagName().split("[, ]+");
                for (String tag : moodTags) {
                    if (!tag.isEmpty()) {
                        moodTagSet.add(tag.trim());
                    }
                }
            }

            // Genre tags 처리
            if (sound.getGenreTagName() != null) {
                String[] genreTags = sound.getGenreTagName().split("[, ]+");
                for (String tag : genreTags) {
                    if (!tag.isEmpty()) {
                        genreTagSet.add(tag.trim());
                    }
                }
            }
        }

        // Save the sets to the SoundServiceDto
        afterTags.setInstTag(instTagSet);
        afterTags.setMoodTag(moodTagSet);
        afterTags.setGenreTag(genreTagSet);

        return afterTags;
    }
}

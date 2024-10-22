package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.SoundServiceDto;
import com.soundbrew.soundbrew.dto.SoundRepositoryDto;
import com.soundbrew.soundbrew.dto.SoundRequestDto;
import com.soundbrew.soundbrew.repository.sound.AlbumMusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SoundService {
    @Autowired
    private AlbumMusicRepository albumMusicRepository;

    // 검색
    // parameter : SoundRequestDto, page
    public SoundServiceDto soundSearch(SoundRequestDto soundRequestDto, Pageable pageable){
        List<SoundRepositoryDto> before = albumMusicRepository.search(soundRequestDto, pageable);

        List<SoundRepositoryDto> afterSearch = replaceCommaWithSpace(before);
        SoundServiceDto after = replaceTagsToArray(before);

        after.setSoundRepositoryDto(afterSearch);
        return after;
    }

    // ","로 이어진 태그문자열을 " " 로 바꾸어 프론트에서 예쁘게 보이기 위한 재단
    public List<SoundRepositoryDto> replaceCommaWithSpace(List<SoundRepositoryDto> sounds) {
        for (SoundRepositoryDto sound : sounds) {
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
    public SoundServiceDto replaceTagsToArray(List<SoundRepositoryDto> sounds) {
        SoundServiceDto afterTags = new SoundServiceDto();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SoundRepositoryDto sound : sounds) {
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

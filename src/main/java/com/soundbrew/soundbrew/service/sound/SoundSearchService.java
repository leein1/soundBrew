package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchFilterDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchResultDto;
import com.soundbrew.soundbrew.dto.sound.SoundSearchRequestDto;
import com.soundbrew.soundbrew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SoundSearchService {
    @Autowired
    private AlbumMusicService albumMusicService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlbumService albumService;

    public SoundSearchFilterDto soundSearch(SoundSearchRequestDto soundSearchRequestDto, Pageable pageable){
        List<SoundSearchResultDto> before = albumMusicService.readTatal(soundSearchRequestDto,pageable);

        SoundSearchFilterDto after = replaceTagsToArray(before);
        List<SoundSearchResultDto> afterSearch = replaceCommaWithSpace(before);

        after.setSoundSearchResultDto(afterSearch);
        return after;
    }

    public AlbumDto readAlbumByArtistName(String nickName){
        int userid = userRepository.findByNickname(nickName).orElseThrow();

        return albumService.readAlbumWithUserId(userid);
    }

    public AlbumDto readAlbum(){
        return albumService.readAlbum();
    }

    // ","로 이어진 태그문자열을 " " 로 바꾸어 프론트에서 예쁘게 보이기 위한 재단
    public List<SoundSearchResultDto> replaceCommaWithSpace(List<SoundSearchResultDto> sounds) {
        for (SoundSearchResultDto sound : sounds) {
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
    public SoundSearchFilterDto replaceTagsToArray(List<SoundSearchResultDto> sounds) {
        SoundSearchFilterDto afterTags = new SoundSearchFilterDto();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SoundSearchResultDto sound : sounds) {
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

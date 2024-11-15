package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.TagsDto;

import java.util.Optional;

public interface TagsService {
    void createInstTag(TagsDto tagsDto);
    void createMoodTag(TagsDto tagsDto);
    void createGenreTag(TagsDto tagsDto);
    void updateInstrumentTagSpelling(String beforeName, String afterName);
    void updateMoodTagSpelling(String beforeName, String afterName);
    void updateGenreTagSpelling(String beforeName, String afterName);
    Optional<TagsDto> readTags();
    Optional<?> readTagsByArtistName(String userName);
    void updateSoundTags(int musicId, TagsDto tagsDto);
    void createSoundTags(Music music, TagsDto tagsDto);
}

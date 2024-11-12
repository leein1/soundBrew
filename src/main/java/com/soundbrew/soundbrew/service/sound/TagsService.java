package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.GenreTagDto;
import com.soundbrew.soundbrew.dto.sound.TagsDto;
import com.soundbrew.soundbrew.dto.sound.MoodTagDto;

public interface TagsService {
    public void createInstTag(TagsDto tagsDto);
    void createMoodTag(TagsDto tagsDto);
    void createGenreTag(TagsDto tagsDto);
    public void updateInstrumentTagSpelling(String beforeName, String afterName);
    public void updateMoodTagSpelling(String beforeName, String afterName);
    public void updateGenreTagSpelling(String beforeName, String afterName);
    public MoodTagDto readMoodTag();
    public GenreTagDto readGenreTag();
    public TagsDto readInstTag();
    void updateSoundTags(int musicId, TagsDto tagsDto);
    void createSoundTags(Music music, TagsDto tagsDto);
}

package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicGenreTag;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTag;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicMoodTagRepository extends JpaRepository<MusicMoodTag, MusicMoodTagId> {
    List<MusicMoodTag> findByIdMusicId(int musicId);
    List<MusicMoodTag> findByIdMoodTagId(int moodTagId);
}

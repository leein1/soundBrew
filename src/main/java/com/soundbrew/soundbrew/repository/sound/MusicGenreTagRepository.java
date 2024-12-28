package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.domain.sound.MusicGenreTag;
import com.soundbrew.soundbrew.domain.sound.MusicGenreTagId;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicGenreTagRepository extends JpaRepository<MusicGenreTag, MusicGenreTagId> {
    List<MusicGenreTag> findByIdMusicId(int musicId);
    List<MusicGenreTag> findByIdGenreTagId(int moodTagId);
    void deleteByIdMusicId(int musicId);
}

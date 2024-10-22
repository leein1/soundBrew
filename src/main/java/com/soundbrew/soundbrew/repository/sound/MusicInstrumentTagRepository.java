package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicGenreTag;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTagId;
import com.soundbrew.soundbrew.domain.sound.MusicMoodTag;
import com.soundbrew.soundbrew.dto.MusicInstrumentTagDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicInstrumentTagRepository extends JpaRepository<MusicInstrumentTag,MusicInstrumentTagId> {
    List<MusicInstrumentTag> findByIdMusicId(int musicId);
    List<MusicInstrumentTag> findByIdInstrumentTagId(int instrumentTagId);
}


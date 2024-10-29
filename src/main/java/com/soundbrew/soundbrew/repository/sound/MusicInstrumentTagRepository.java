package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicInstrumentTagRepository extends JpaRepository<MusicInstrumentTag,MusicInstrumentTagId> {
    List<MusicInstrumentTag> findByIdMusicId(int musicId);
    List<MusicInstrumentTag> findByIdInstrumentTagId(int instrumentTagId);
    void deleteByIdMusicId(int musicId);
}


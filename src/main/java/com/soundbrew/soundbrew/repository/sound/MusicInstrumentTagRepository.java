package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTag;
import com.soundbrew.soundbrew.domain.sound.MusicInstrumentTagId;
import com.soundbrew.soundbrew.dto.MusicInstrumentTagDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicInstrumentTagRepository extends JpaRepository<MusicInstrumentTag,MusicInstrumentTagId> {

    @Query("SELECT new com.soundbrew.soundbrew.dto.MusicInstrumentTagDto(" +
            "m.music_id, m.title, m.description, it.instrument_tag_id, it.instrument_tag_name) " +
            "FROM MusicInstrumentTag mit " +
            "JOIN mit.music m " +
            "JOIN mit.instrumentTag it " )
    List<MusicInstrumentTagDto> findAllMusicAndTag();
}


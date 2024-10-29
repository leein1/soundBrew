package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.InstrumentTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface InstrumentTagRepository extends JpaRepository<InstrumentTag, Integer> {
    Optional<InstrumentTag> findByInstrumentTagName(String tagName);
}

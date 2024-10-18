package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.domain.sound.AlbumMusicId;
import com.soundbrew.soundbrew.repository.custom.AlbumMusicRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumMusicRepository extends JpaRepository<AlbumMusic, AlbumMusicId>, AlbumMusicRepositoryCustom {

}

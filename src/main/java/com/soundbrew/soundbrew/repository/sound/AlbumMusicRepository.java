package com.soundbrew.soundbrew.repository.sound;

import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.domain.sound.AlbumMusicId;
import com.soundbrew.soundbrew.repository.sound.custom.AlbumMusicRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumMusicRepository extends JpaRepository<AlbumMusic, AlbumMusicId>, AlbumMusicRepositoryCustom {
    List<AlbumMusic> findByIdAlbumId(int album);
    List<AlbumMusic> findByIdMusicId(int music);
    List<AlbumMusic> findByIdUserId(int user);
}

package com.soundbrew.soundbrew.dto.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.*;

public class SoundFactory {
    public static AlbumMusic albumMusicToEntity(Album album, Music music, User user){
        return AlbumMusic.builder()
                .id(new AlbumMusicId(album.getAlbumId(), music.getUserId(), music.getMusicId()))
                .album(album)
                .music(music)
                .user(user)
                .build();
    }

    public static MusicInstrumentTag musicInstrumentTagToEntity(Music music, InstrumentTag instrumentTag){
        return MusicInstrumentTag.builder()
                .music(music)
                .instrumentTag(instrumentTag)
                .id(new MusicInstrumentTagId(music.getMusicId(), instrumentTag.getInstrumentTagId()))
                .build();
    }

    public static MusicMoodTag musicMoodTagToEntity(Music music, MoodTag moodTag){
        return MusicMoodTag.builder()
                .music(music)
                .moodTag(moodTag)
                .id(new MusicMoodTagId(music.getMusicId(), moodTag.getMoodTagId()))
                .build();
    }

    public static MusicGenreTag musicGenreTagToEntity(Music music, GenreTag genreTag){
        return MusicGenreTag.builder()
                .music(music)
                .genreTag(genreTag)
                .id(new MusicGenreTagId(music.getUserId(), genreTag.getGenreTagId()))
                .build();
    }
}

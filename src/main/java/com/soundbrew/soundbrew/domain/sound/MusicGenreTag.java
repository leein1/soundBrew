package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MusicGenreTag {
    @EmbeddedId
    private MusicGenreTagId id;

    @ManyToOne
    @MapsId("musicId")
    @JoinColumn(name = "musicId")
    private Music music;

    @ManyToOne
    @MapsId("genreTagId")
    @JoinColumn(name = "genreTagId")
    private GenreTag genreTag;
}

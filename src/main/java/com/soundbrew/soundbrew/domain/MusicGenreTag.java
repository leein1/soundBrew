package com.soundbrew.soundbrew.domain;

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
    @MapsId("music_id")
    @JoinColumn(name = "music_id")
    private Music music;

    @ManyToOne
    @MapsId("genre_tag_id")
    @JoinColumn(name = "genre_tag_id")
    private GenreTag genreTag;
}

package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(of = {"genre_tag_id", "genre_tag_name"})
public class GenreTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int genre_tag_id;

    @Column(nullable = false)
    private String genre_tag_name;

    @OneToMany(mappedBy = "genreTag")
    private List<MusicGenreTag> musicGenreTag;
}

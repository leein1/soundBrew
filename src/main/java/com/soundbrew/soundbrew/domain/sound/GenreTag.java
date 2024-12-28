package com.soundbrew.soundbrew.domain.sound;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(of = {"genreTagId", "genreTagName"})
public class GenreTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int genreTagId;

    @Column(nullable = false)
    private String genreTagName;

    @OneToMany(mappedBy = "genreTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenreTag> musicGenreTag;

    public void update(String genreTagName){
        this.genreTagName = genreTagName;
    }

//    public void update(TagNameDto tagNameDto){
//        this.genreTagName = tagNameDto.getAfterName();
//    }
}

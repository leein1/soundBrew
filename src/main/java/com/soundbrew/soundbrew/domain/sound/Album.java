package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int albumId;

    @Column(nullable = false)
    private int userId;

    @Column(length = 255, nullable = false)
    private String albumName;

    @Column(length = 255)
    private String albumArtPath;

    @Column(length = 500)
    private String description;

    public void update(String albumName, String description){
        this.albumName = albumName;
        this.description = description;
    }

}

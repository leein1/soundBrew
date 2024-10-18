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
    private int album_id;

    @Column(nullable = false)
    private int user_id;

    @Column(length = 255, nullable = false)
    private String album_name;

    @Column(length = 255)
    private String album_art_path;

    @Column(length = 500)
    private String description;


}

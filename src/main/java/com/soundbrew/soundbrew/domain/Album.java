package com.soundbrew.soundbrew.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Album {

    private int album_id;

    private int user_id;

    private String album_name;

    private String albujm_art_path;

    private String description;

    private LocalDateTime create_date;


}

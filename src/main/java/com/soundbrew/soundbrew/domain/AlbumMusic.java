package com.soundbrew.soundbrew.domain;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlbumMusic {

    private int album_id;

    private int user_id;

    private int music_id;
}

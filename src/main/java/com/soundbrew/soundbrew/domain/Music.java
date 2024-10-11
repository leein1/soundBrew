package com.soundbrew.soundbrew.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Music {

    private int music_id;

    private int user_id;

    private String title;

    private String file_path;

    private int price;

    private String description;

    private LocalDateTime create_date;
}

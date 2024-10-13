package com.soundbrew.soundbrew.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Music extends BaseEntity{

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int music_id;

    @Column(nullable = false)
    private int user_id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String file_path;

    @Column(nullable = false)
    private int price;

    @Column(length=500, nullable = false)
    private String description;


    // BassEntity 상속, 및 create_date 없으니 확인해볼것.
    //private LocalDateTime create_date;

}

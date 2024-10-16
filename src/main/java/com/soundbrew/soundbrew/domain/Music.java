package com.soundbrew.soundbrew.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"title", "user_id"})
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

    @OneToMany(mappedBy = "music")
    private List<MusicInstrumentTag> musicInstrumentTag ; // 태그와의 관계 추가


}

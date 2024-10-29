package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"title", "userId"})
@Entity
public class Music extends BaseEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int musicId;

    @Column(nullable = false)
    private int userId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String filePath;

    @Column(nullable = false)
    private int price;

    @Column(length=500, nullable = false)
    private String description;

    @Column(nullable = false)
    private String soundType;

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicInstrumentTag> musicInstrumentTag; // 태그와의 관계 추가

    @OneToMany(mappedBy = "music" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicMoodTag> musicMoodTag ;// 태그와의 관계 추가
//
    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenreTag> musicGenreTag ; // 태그와의 관계 추가

    public void update(String title, String description, String soundType){
        this.title = title;
        this.description = description;
        this.soundType = soundType;
    }

}

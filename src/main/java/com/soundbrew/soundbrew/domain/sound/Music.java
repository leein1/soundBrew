package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.BaseEntity;
import com.soundbrew.soundbrew.domain.user.User;
import lombok.*;

import javax.persistence.*;
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

    @Column(nullable = true, length = 50)
    private String nickname;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String filePath;

    @Column(nullable = false)
    private int price;

    @Column(length=500, nullable = false)
    private String description;

    @Column(nullable = false)
    private String soundType;

    @Column
    private int download;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false) // userId와 User의 관계 설정
    private User user;

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicInstrumentTag> musicInstrumentTag; // 태그와의 관계 추가

    @OneToMany(mappedBy = "music" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicMoodTag> musicMoodTag ;// 태그와의 관계 추가
//
    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenreTag> musicGenreTag ; // 태그와의 관계 추가

    @OneToMany(mappedBy = "music", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<AlbumMusic> albumMusics = new ArrayList<>();

    
    public void update(String title, String description, String soundType){
        this.title = title;
        this.description = description;
        this.soundType = soundType;
    }

//    public void update(Music music){
//        this.title = music.getTitle();
//        this.description = music.getDescription();
//        this.soundType = music.getSoundType();
//    }

}

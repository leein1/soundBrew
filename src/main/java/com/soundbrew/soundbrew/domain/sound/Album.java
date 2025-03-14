package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "albumMusic")
@Entity
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int albumId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = true, length = 50)
    private String nickname;

    @Column(length = 255, nullable = false)
    private String albumName;

    @Column(length = 255)
    private String albumArtPath;

    @Column(length = 500)
    private String description;

    @Column
    private int download;

    @Column(length = 1)
    private int verify;
//    @ManyToOne
//    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false) // userId와 User의 관계 설정
//    private User user;  // User와의 관계를 설정

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumMusic> albumMusic;

    public void update(String albumName, String description){
        this.albumName = albumName;
        this.description = description;
    }

    public void verify(int verify){
        this.verify= verify;
    }
}

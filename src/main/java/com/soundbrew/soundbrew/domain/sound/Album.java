package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.BaseEntity;
import com.soundbrew.soundbrew.domain.User;
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

    @Column(nullable = true, length = 50)
    private String nickname;

    @Column(length = 255, nullable = false)
    private String albumName;

    @Column(length = 255)
    private String albumArtPath;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false) // userId와 User의 관계 설정
    private User user;  // User와의 관계를 설정

    public void update(String albumName, String description){
        this.albumName = albumName;
        this.description = description;
    }

}

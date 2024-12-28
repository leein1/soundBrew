package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class AlbumMusic {
    @EmbeddedId
    private AlbumMusicId id;

    @ManyToOne
    @MapsId("albumId")  // AlbumMusicId의 albumId를 매핑
    @JoinColumn(name = "albumId")
    private Album album;

    @ManyToOne
    @MapsId("musicId")  // AlbumMusicId의 musicId를 매핑
    @JoinColumn(name = "musicId")
    private Music music;

    @ManyToOne
    @MapsId("userId")   // AlbumMusicId의 userId를 매핑
    @JoinColumn(name = "userId")
    private User user;    // User 엔티티를 참조하는 필드 추가
}

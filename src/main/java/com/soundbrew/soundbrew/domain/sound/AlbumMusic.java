package com.soundbrew.soundbrew.domain.sound;

import com.soundbrew.soundbrew.domain.User;
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
    @MapsId("album_id")  // AlbumMusicId의 albumId를 매핑
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @MapsId("music_id")  // AlbumMusicId의 musicId를 매핑
    @JoinColumn(name = "music_id")
    private Music music;

    @ManyToOne
    @MapsId("user_id")   // AlbumMusicId의 userId를 매핑
    @JoinColumn(name = "user_id")
    private User user;    // User 엔티티를 참조하는 필드 추가
}

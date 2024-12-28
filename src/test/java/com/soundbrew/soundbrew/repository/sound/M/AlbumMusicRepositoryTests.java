package com.soundbrew.soundbrew.repository.sound.M;

import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.domain.sound.AlbumMusicId;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.repository.sound.AlbumMusicRepository;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Log4j2
public class AlbumMusicRepositoryTests {
    @Autowired
    private AlbumMusicRepository albumMusicRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;

    private AlbumMusicId albumMusicId;

    @Test
    @Transactional
    void insert(){
        Album album = Album.builder()
                .userId(2)
                .albumName("testcode")
                .albumArtPath("/test/test")
                .description("test description")
                .build();
        albumRepository.save(album);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        User user = User.builder()
                .subscription_id(1)
                .name("user")
                .nickname("u" )
                .password("password")
                .phonenumber("010222222")
                .email("insert_test@test.com")
                .build();
        userRepository.save(user);

        albumMusicId = AlbumMusicId.builder()
                .albumId(album.getAlbumId())
                .musicId(music.getMusicId())
                .userId(user.getUserId())
                .build();

        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(albumMusicId)
                .album(album)
                .music(music)
                .user(user)
                .build();

        albumMusicRepository.save(albumMusic);

        log.info("=======");
        log.info(albumMusic.toString());
        log.info("=======");
    }

    @Test
    @Transactional
    void delete(){
        Album album = Album.builder()
                .userId(2)
                .albumName("testcode")
                .albumArtPath("/test/test")
                .description("test description")
                .build();
        albumRepository.save(album);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        User user = User.builder()
                .subscription_id(1)
                .name("user")
                .nickname("u" )
                .password("password")
                .phonenumber("010222222")
                .email("insert_test@test.com")
                .build();
        userRepository.save(user);

        albumMusicId = AlbumMusicId.builder()
                .albumId(album.getAlbumId())
                .musicId(music.getMusicId())
                .userId(user.getUserId())
                .build();

        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(albumMusicId)
                .album(album)
                .music(music)
                .user(user)
                .build();

        albumMusicRepository.save(albumMusic);

        albumMusicRepository.deleteById(albumMusicId);
        assertFalse(albumMusicRepository.existsById(albumMusicId));
    }

    @Test
    @Transactional
    void list(){
        Album album = Album.builder()
                .userId(2)
                .albumName("testcode")
                .albumArtPath("/test/test")
                .description("test description")
                .build();
        albumRepository.save(album);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        User user = User.builder()
                .subscription_id(1)
                .name("user")
                .nickname("u" )
                .password("password")
                .phonenumber("010222222")
                .email("insert_test@test.com")
                .build();
        userRepository.save(user);

        albumMusicId = AlbumMusicId.builder()
                .albumId(album.getAlbumId())
                .musicId(music.getMusicId())
                .userId(user.getUserId())
                .build();

        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(albumMusicId)
                .album(album)
                .music(music)
                .user(user)
                .build();

        albumMusicRepository.save(albumMusic);

        assertNotNull(albumMusicRepository.findById(albumMusicId));
    }

    @Test
    @Transactional
    void musidId(){
        Album album = Album.builder()
                .userId(2)
                .albumName("testcode")
                .albumArtPath("/test/test")
                .description("test description")
                .build();
        albumRepository.save(album);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        User user = User.builder()
                .subscription_id(1)
                .name("user")
                .nickname("u" )
                .password("password")
                .phonenumber("010222222")
                .email("insert_test@test.com")
                .build();
        userRepository.save(user);

        albumMusicId = AlbumMusicId.builder()
                .albumId(album.getAlbumId())
                .musicId(music.getMusicId())
                .userId(user.getUserId())
                .build();

        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(albumMusicId)
                .album(album)
                .music(music)
                .user(user)
                .build();

        albumMusicRepository.save(albumMusic);

        List<AlbumMusic> result = albumMusicRepository.findByIdMusicId(1);
        result.forEach(value -> value.getId());
    }

    @Test
    @Transactional
    void albumId(){
        Album album = Album.builder()
                .userId(2)
                .albumName("testcode")
                .albumArtPath("/test/test")
                .description("test description")
                .build();
        albumRepository.save(album);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        User user = User.builder()
                .subscription_id(1)
                .name("user")
                .nickname("u" )
                .password("password")
                .phonenumber("010222222")
                .email("insert_test@test.com")
                .build();
        userRepository.save(user);

        albumMusicId = AlbumMusicId.builder()
                .albumId(album.getAlbumId())
                .musicId(music.getMusicId())
                .userId(user.getUserId())
                .build();

        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(albumMusicId)
                .album(album)
                .music(music)
                .user(user)
                .build();

        albumMusicRepository.save(albumMusic);

        List<AlbumMusic> result = albumMusicRepository.findByIdAlbumId(1);
        result.forEach(value -> value.getId());
    }

    @Test
    @Transactional
    void userId(){
        Album album = Album.builder()
                .userId(2)
                .albumName("testcode")
                .albumArtPath("/test/test")
                .description("test description")
                .build();
        albumRepository.save(album);

        Music music = Music.builder()
                .title("fury")
                .filePath("/file/test/music_path_test_hello_fury")
                .price(3)
                .description("Jonsi의 fury 팔세토가 돋보입니다.")
                .userId(2)
                .soundType("music")
                .build();
        musicRepository.save(music);

        User user = User.builder()
                .subscription_id(1)
                .name("user")
                .nickname("u" )
                .password("password")
                .phonenumber("010222222")
                .email("insert_test@test.com")
                .build();
        userRepository.save(user);

        albumMusicId = AlbumMusicId.builder()
                .albumId(album.getAlbumId())
                .musicId(music.getMusicId())
                .userId(user.getUserId())
                .build();

        AlbumMusic albumMusic = AlbumMusic.builder()
                .id(albumMusicId)
                .album(album)
                .music(music)
                .user(user)
                .build();

        albumMusicRepository.save(albumMusic);

        List<AlbumMusic> result = albumMusicRepository.findByIdUserId(1);
        result.forEach(value -> value.getId());
    }



}

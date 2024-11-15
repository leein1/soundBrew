package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.domain.sound.AlbumMusic;
import com.soundbrew.soundbrew.domain.sound.Music;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.repository.sound.AlbumMusicRepository;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soundbrew.soundbrew.dto.sound.SoundFactory.albumMusicToEntity;

@Service
@AllArgsConstructor
@Log4j2
public class SoundServiceImpl implements SoundService{
    private final AlbumMusicRepository albumMusicRepository;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final SoundProcessor soundProcessor;
    private final TagsServiceImpl tagsServiceImpl;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void createSound(int checkedUserId,AlbumDto albumDto, MusicDto musicDto, TagsDto tagsDto){
        User checkedUser = userRepository.findById(2).orElseThrow();
        albumDto.setUserId(checkedUser.getUserId());
        musicDto.setUserId(checkedUser.getUserId());
        Album album = albumRepository.save(albumDto.toEntity());
        Music music = musicRepository.save(musicDto.toEntity());
        albumMusicRepository.save(albumMusicToEntity(album,music,checkedUser));
        tagsServiceImpl.createSoundTags(music,tagsDto);
    }

    @Override
    public Optional<SoundSearchFilterDto> soundSearch(SoundSearchRequestDto soundSearchRequestDto, Pageable pageable) {
        List<SoundSearchResultDto> before = albumMusicRepository.search(soundSearchRequestDto, pageable);
        //process
        SoundSearchFilterDto after = soundProcessor.replaceTagsToArray(before);
        List<SoundSearchResultDto> afterSearch = soundProcessor.replaceCommaWithSpace(before);
        after.setSoundSearchResultDto(afterSearch);

        return Optional.of(after);
    }

    @Override
    public Optional<List<AlbumDto>> readAlbumByArtistName(String nickName) {
        Optional<Integer> userId = userRepository.findByNickname(nickName);

        if(userId.isEmpty()){return Optional.empty();}

        List<Album> result = albumRepository.findByUserId(userId.get()).get();

        return result.isEmpty() ? Optional.empty():
                Optional.of(result.stream()
                .map(album -> modelMapper.map(album, AlbumDto.class))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<MusicDto>> readMusicByArtistName(String nickName) {
        Optional<Integer> userId = userRepository.findByNickname(nickName);

        if(userId.isEmpty()){  return Optional.empty();}

        List<Music> result = musicRepository.findByUserId(userId.get()).get();

        return result.isEmpty() ? Optional.empty():
                Optional.of(result.stream()
                .map(music -> modelMapper.map( music,MusicDto.class))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AlbumDto>> readAlbum() {
        List<Album> albums = albumRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        return albums.isEmpty() ? Optional.empty():
                Optional.of(albums.stream()
                .map(album -> modelMapper.map(album, AlbumDto.class))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<MusicDto>> readMusic() {
        List<Music> music = musicRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        return music.isEmpty() ? Optional.empty():
                Optional.of(music.stream()
                .map(result -> modelMapper.map(result, MusicDto.class))
                .collect(Collectors.toList()));
    }

    @Override
    public void updateAlbum(int albumId, AlbumDto albumDto) {
        Album modify = albumRepository.findById(albumId).orElseThrow();
        modify.update(albumDto.getAlbumName(), albumDto.getDescription());
    }

    @Override
    public void updateMusic(int musicId, MusicDto musicDto) {
        Music modify = musicRepository.findById(musicId).orElseThrow();
        modify.update(musicDto.getTitle(),musicDto.getDescription(), musicDto.getSoundType());
    }

    @Override
    public void deleteAlbum(int albumId) {
        albumRepository.deleteById(albumId);
    }

    @Override
    public void deleteMusic(int musicId) {
        musicRepository.deleteById(musicId);
    }
}

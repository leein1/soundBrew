package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public Album createAlbum(AlbumDto albumDto){
        Album album = albumDto.toEntity();
        return albumRepository.save(album);
    }

    public void updateAlbum(Album album, AlbumDto albumDto){
        album.update(albumDto.getAlbumName(), albumDto.getDescription());
    }

    public void deleteAlbum(int albumId){
        albumRepository.deleteById(albumId);
    }

    public AlbumDto readAlbumWithUserId(int userId){
        List<Album> result = albumRepository.findByUserId(userId).orElseThrow();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(result, AlbumDto.class);
    }

    public AlbumDto readAlbum(){
        List<Album> result = albumRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(result, AlbumDto.class);
    }

}

package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.domain.sound.Album;
import com.soundbrew.soundbrew.dto.sound.AlbumDto;
import com.soundbrew.soundbrew.repository.sound.AlbumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public Album createAlbum(AlbumDto albumDto){
        Album album = albumDto.toEntity();
        return albumRepository.save(album);
    }

    public void updateAlbum(int albumId, AlbumDto albumDto){
        Album modify = albumRepository.findById(albumId).orElseThrow();
        modify.update(albumDto.getAlbumName(), albumDto.getDescription());
    }

    public void deleteAlbum(int albumId){
        albumRepository.deleteById(albumId);
    }

    public List<AlbumDto> readAlbumWithUserId(int userId){
        List<Album> result = albumRepository.findByUserId(userId).orElseThrow();
        ModelMapper modelMapper = new ModelMapper();

        List<AlbumDto> albumDtos = result.stream()
                .map(album -> modelMapper.map(album, AlbumDto.class))
                .collect(Collectors.toList());
        return albumDtos;
    }

    public AlbumDto readAlbum(){
        List<Album> result = albumRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(result, AlbumDto.class);
    }


}

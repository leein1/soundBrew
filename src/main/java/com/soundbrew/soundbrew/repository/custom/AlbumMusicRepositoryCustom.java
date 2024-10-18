package com.soundbrew.soundbrew.repository.custom;


import com.soundbrew.soundbrew.dto.AlbumMusicDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlbumMusicRepositoryCustom {
    List<AlbumMusicDto> search(String nickname, Integer musicId, Integer albumId, List<String> instTags, List<String> moodTags, List<String> genreTags, Pageable pageable);
}

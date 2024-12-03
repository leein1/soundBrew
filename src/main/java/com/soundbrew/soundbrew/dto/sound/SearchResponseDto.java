package com.soundbrew.soundbrew.dto.sound;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
@Builder
public class SearchResponseDto {
    private List<SearchTotalResultDto> searchTotalResultDto;
    private Set<String> instTag;
    private Set<String> moodTag;
    private Set<String> genreTag;
    private String keyword;
    private List<AlbumDto> otherAlbums;
    private Pageable pageable; // 필요하다면 추가
}

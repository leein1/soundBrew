package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.DTOFilteringFactory;
import com.soundbrew.soundbrew.dto.RequestDto;
import com.soundbrew.soundbrew.dto.ResponseDto;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.sound.AlbumMusicRepository;
import com.soundbrew.soundbrew.repository.sound.MusicRepository;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.soundbrew.soundbrew.dto.DTOFilteringFactory.hideSearchTotalResultDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class SoundsServiceImpl implements SoundsService{
    @Value("${player.sounds}") private String fileDirectory;

    private final AlbumMusicRepository albumMusicRepository;
    private final MusicRepository musicRepository;
    private final SoundProcessor soundProcessor;
    private static final long MAX_RANGE_SIZE = 5 * 1024 * 1024; // 5MB

//    @Override
//    public ResponseDto<SoundStreamDto> streamSound(HttpRange range, String fileName) throws IOException {
//        Path filePath = Path.of(fileDirectory+"/"+fileName);
//        if (!Files.exists(filePath) || !Files.isReadable(filePath)) return ResponseDto.<SoundStreamDto>withMessage().message("재생할 음원을 읽지못했습니다.").build();
//
//        long fileLength = Files.size(filePath);
//        long start = range.getRangeStart(fileLength);
//        long end = Math.min(start + MAX_RANGE_SIZE - 1, fileLength - 1);
//        if (start >= fileLength) return ResponseDto.<SoundStreamDto>withMessage().message("음원재생 길이를 초과하는 요청입니다.").build();
//
//        long rangeLength = end - start + 1;
//        byte[] data = new byte[(int) rangeLength];
//
//        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
//            raf.seek(start);
//            raf.read(data, 0, (int) rangeLength);
//        }
//        // 데이터 반환
//        return ResponseDto.<SoundStreamDto>withSingleData().dto(new SoundStreamDto(data, start, end, fileLength)).build();
//    }
    @Override
    public ResponseDto<SoundStreamDto> streamSound(HttpRange range, String fileName) throws IOException {
        Path filePath = Path.of(fileDirectory + "/" + fileName);

        long fileLength = Files.size(filePath);
        long start = range.getRangeStart(fileLength);
        long end = Math.min(start + MAX_RANGE_SIZE - 1, fileLength - 1);

        long rangeLength = end - start + 1;
        byte[] data = new byte[(int) rangeLength];

        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            raf.seek(start);
            raf.read(data, 0, (int) rangeLength);
        }

        return ResponseDto.<SoundStreamDto>withSingleData().dto(new SoundStreamDto(data, start, end, fileLength)).build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> totalSoundSearch(RequestDto requestDto) {
        Optional<Page<SearchTotalResultDto>> before = albumMusicRepository.search(requestDto,"sound");
        if(before.get().isEmpty()) return ResponseDto.<SearchTotalResultDto>builder().dtoList(Collections.emptyList()).build();

        return ResponseDto.<SearchTotalResultDto>withAll()
                .requestDto(requestDto)
                .dtoList(soundProcessor.replaceCommaWithSpace(before.get().getContent()).stream()
                    .map(DTOFilteringFactory::hideSearchTotalResultDto).toList())
                .total((int) before.get().getTotalElements())
                .build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> totalAlbumSearch(RequestDto searchRequestDto) {
        Optional<Page<SearchTotalResultDto>> before = albumMusicRepository.search(searchRequestDto, "album");
        if(before.get().isEmpty()) return ResponseDto.<SearchTotalResultDto>builder().dtoList(Collections.emptyList()).build();

        return  ResponseDto.<SearchTotalResultDto>withAll()
                .dtoList(before.get().getContent().stream()
                        .map(DTOFilteringFactory::hideSearchTotalResultDto).toList())
                .requestDto(searchRequestDto)
                .build();
    }

    @Override
    public ResponseDto<TagsDto> totalTagsSearch(List<SearchTotalResultDto> sounds) {
        TagsDto tagsDto = new TagsDto();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SearchTotalResultDto sound : sounds) {
            soundProcessor.addTagsToSet(sound.getInstrumentTagName(), instTagSet);
            soundProcessor.addTagsToSet(sound.getMoodTagName(), moodTagSet);
            soundProcessor.addTagsToSet(sound.getGenreTagName(), genreTagSet);
        }

        tagsDto.setInstrument(new ArrayList<>(instTagSet));
        tagsDto.setMood(new ArrayList<>(moodTagSet));
        tagsDto.setGenre(new ArrayList<>(genreTagSet));

        return ResponseDto.<TagsDto>withSingleData().dto(tagsDto).build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getSoundOne(String nickname, String title) {
        Optional<SearchTotalResultDto> musicPage = musicRepository.soundOne(nickname,title);
        if(!musicPage.isPresent()) return ResponseDto.<SearchTotalResultDto>withMessage().message("찾은시는 음원이 없습니다.").build();

        return ResponseDto.<SearchTotalResultDto>withSingleData().dto(hideSearchTotalResultDto(musicPage.get())).build();
    }

    @Override
    public ResponseDto<SearchTotalResultDto> getAlbumOne(String nickname, String albumName, RequestDto requestDto) {
        Optional<Page<SearchTotalResultDto>> albumPage = albumMusicRepository.getAlbumOne(nickname,albumName,requestDto);
        if(albumPage.get().isEmpty()) return ResponseDto.<SearchTotalResultDto>withMessage().message("찾으신 앨범의 정보가 없습니다.").build();

        return  ResponseDto.<SearchTotalResultDto>withAll()
                .dtoList(soundProcessor.replaceCommaWithSpace(albumPage.get().getContent()).stream()
                    .map(DTOFilteringFactory::hideSearchTotalResultDto).toList())
                .total((int) albumPage.get().getTotalElements())
                .build();
    }
}

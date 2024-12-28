package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.dto.DTOFilteringFactory;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
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

import static com.soundbrew.soundbrew.dto.DTOFilteringFactory.hideSearchTotalResultDTO;

@Service
@RequiredArgsConstructor
@Log4j2
public class SoundsServiceImpl implements SoundsService{
    @Value("${player.sounds}") private String fileDirectory;

    private final AlbumMusicRepository albumMusicRepository;
    private final MusicRepository musicRepository;
    private final SoundProcessor soundProcessor;
    private static final long MAX_RANGE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public ResponseDTO<SearchTotalResultDTO> totalSoundSearch(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.search(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        return null;
//        return ResponseDTO.<SearchTotalResultDTO>withAll()
//                .requestDTO(requestDTO)
//                .dtoList(soundProcessor.replaceCommaWithSpace(before.get().getContent()).stream()
//                    .map(DTOFilteringFactory::hideSearchTotalResultDTO).toList())
//                .total((int) before.get().getTotalElements())
//                .build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> totalAlbumSearch(RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> before = albumMusicRepository.searchAlbum(requestDTO);
        if(before.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>builder().dtoList(Collections.emptyList()).build();

        log.info(before.get().stream().toList());

        return null;
//        return  ResponseDTO.<SearchTotalResultDTO>withAll()
//                .dtoList(before.get().getContent().stream()
//                        .map(DTOFilteringFactory::hideSearchTotalResultDTO).toList())
//                .requestDTO(requestDTO)
//                .build();
    }

    @Override
    public ResponseDTO<TagsDTO> totalTagsSearch(List<SearchTotalResultDTO> sounds) {
        TagsDTO tagsDTO = new TagsDTO();
        Set<String> instTagSet = new HashSet<>();
        Set<String> moodTagSet = new HashSet<>();
        Set<String> genreTagSet = new HashSet<>();

        for (SearchTotalResultDTO sound : sounds) {
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getInstrumentTagName(), instTagSet);
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getMoodTagName(), moodTagSet);
            soundProcessor.addTagsToSet(sound.getTagsStreamDTO().getGenreTagName(), genreTagSet);
        }

        tagsDTO.setInstrument(new ArrayList<>(instTagSet));
        tagsDTO.setMood(new ArrayList<>(moodTagSet));
        tagsDTO.setGenre(new ArrayList<>(genreTagSet));

        return ResponseDTO.<TagsDTO>withSingleData().dto(tagsDTO).build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getSoundOne(String nickname, String title) {
        Optional<SearchTotalResultDTO> musicPage = musicRepository.soundOne(nickname,title);
        if(musicPage.isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 음원이 없습니다.").build();

        return ResponseDTO.<SearchTotalResultDTO>withSingleData().dto(hideSearchTotalResultDTO(musicPage.get())).build();
    }

    @Override
    public ResponseDTO<SearchTotalResultDTO> getAlbumOne(String nickname, String albumName, RequestDTO requestDTO) {
        Optional<Page<SearchTotalResultDTO>> albumPage = albumMusicRepository.albumOne(nickname,albumName, requestDTO);
        if(albumPage.get().isEmpty()) return ResponseDTO.<SearchTotalResultDTO>withMessage().message("찾으시는 앨범의 정보가 없습니다.").build();

        return null;
//        return  ResponseDTO.<SearchTotalResultDTO>withAll()
//                .dtoList(soundProcessor.replaceCommaWithSpace(albumPage.get().getContent()).stream()
//                    .map(DTOFilteringFactory::hideSearchTotalResultDTO).toList())
//                .total((int) albumPage.get().getTotalElements())
//                .build();
    }

        @Override
    public ResponseDTO<SoundStreamDTO> streamSound(HttpRange range, String fileName) throws IOException {
        Path filePath = Path.of(fileDirectory+"/"+fileName);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) return ResponseDTO.<SoundStreamDTO>withMessage().message("재생할 음원을 읽지못했습니다.").build();

        long fileLength = Files.size(filePath);
        long start = range.getRangeStart(fileLength);
        long end = Math.min(start + MAX_RANGE_SIZE - 1, fileLength - 1);
        if (start >= fileLength) return ResponseDTO.<SoundStreamDTO>withMessage().message("음원재생 길이를 초과하는 요청입니다.").build();

        long rangeLength = end - start + 1;
        byte[] data = new byte[(int) rangeLength];

        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            raf.seek(start);
            raf.read(data, 0, (int) rangeLength);
        }
        // 데이터 반환
        return ResponseDTO.<SoundStreamDTO>withSingleData().dto(new SoundStreamDTO(data, start, end, fileLength)).build();
    }

}

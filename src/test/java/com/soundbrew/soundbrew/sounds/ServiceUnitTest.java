package com.soundbrew.soundbrew.sounds;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.domain.user.User;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.handler.custom.ResourceOwnershipException;
import com.soundbrew.soundbrew.repository.sound.*;
import com.soundbrew.soundbrew.repository.user.UserRepository;
import com.soundbrew.soundbrew.service.authentication.SoundOwnershipCheckService;
import com.soundbrew.soundbrew.service.SoundsServiceImpl;
import com.soundbrew.soundbrew.service.TagsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
public class ServiceUnitTest {

    @InjectMocks
    private SoundsServiceImpl soundsService;

    @Mock private AlbumRepository albumRepository;
    @Mock private MusicRepository musicRepository;
    @Mock private AlbumMusicRepository albumMusicRepository;
    @Mock private TagsService tagsService;
    @Mock private InstrumentTagRepository instrumentTagRepository;
    @Mock private MoodTagRepository moodTagRepository;
    @Mock private GenreTagRepository genreTagRepository;
    @Mock private UserRepository userRepository;
    @Spy private SoundOwnershipCheckService soundOwnershipCheckService = new SoundOwnershipCheckService();
    @Spy private ModelMapper modelMapper;


    @Test
    void testTotalSoundSearchEmpty() {
        //given
        RequestDTO requestDTO = new RequestDTO();

        //when
        when(albumMusicRepository.search(any(RequestDTO.class)))
                .thenReturn(Optional.of(Page.empty()));

        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalSoundSearch(requestDTO);

        //then
        assertNotNull(response, "응답은 null이 아니어야 합니다.");
        assertNotNull(response.getDtoList(), "dtoList는 null이 아니어야 합니다.");
        assertTrue(response.getDtoList().isEmpty(), "검색 결과가 없어야 합니다.");
    }

    @Test
    void testTotalAlbumSearchSuccess() {
        RequestDTO requestDTO = new RequestDTO();
        SearchTotalResultDTO dummyResult = new SearchTotalResultDTO();
        Page<SearchTotalResultDTO> page = new PageImpl<>(List.of(dummyResult));

        when(albumMusicRepository.searchAlbum(any(RequestDTO.class)))
                .thenReturn(Optional.of(page));

        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalAlbumSearch(requestDTO);

        assertNotNull(response, "응답은 null이 아니어야 합니다.");
        assertFalse(response.getDtoList().isEmpty(), "전체 앨범 검색 결과가 있어야 합니다.");
    }

    @Test
    void testTotalAlbumSearchSuccess2() {
        RequestDTO requestDTO = new RequestDTO();

        when(albumMusicRepository.searchAlbum(any(RequestDTO.class)))
                .thenReturn(Optional.of(Page.empty()));

        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalAlbumSearch(requestDTO);

        assertNotNull(response, "응답은 null이 아니어야 합니다.");
        assertTrue(response.getDtoList().isEmpty(), "앨범 검색 결과가 없어야 합니다.");
    }

    @Test
    void testGetSoundOneSuccess() {
        String nickname = "mele";
        String title = "도서관";

        MusicDTO dummyMusicDTO = new MusicDTO();
        dummyMusicDTO.setTitle("도서관");
        SearchTotalResultDTO dummyResult = new SearchTotalResultDTO();
        dummyResult.setMusicDTO(dummyMusicDTO);

        when(musicRepository.soundOne(nickname, title))
                .thenReturn(Optional.of(dummyResult));

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(nickname, title);

        assertNotNull(response.getDto(), "검색 결과가 있어야 합니다.");
        assertEquals(title, response.getDto().getMusicDTO().getTitle(), "음원 제목이 일치해야 합니다.");
    }

    @Test
    void testGetSoundOneNotFound() {
        String nickname = "artistX";
        String title = "없는음원";

        when(musicRepository.soundOne(nickname, title))
                .thenReturn(Optional.empty());

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(nickname, title);

        assertNull(response.getDto(), "검색 결과가 없어야 합니다.");
    }

    @Test
    void testGetAlbumOneSuccess() {
        // given
        String nickname = "mele";
        String albumName = "도서관 앨범";
        RequestDTO requestDTO = new RequestDTO();
        SearchTotalResultDTO dummyResult = new SearchTotalResultDTO();
        Page<SearchTotalResultDTO> page = new PageImpl<>(List.of(dummyResult));
        Optional<Page<SearchTotalResultDTO>> dummyList = Optional.of(page);

        when(albumMusicRepository.albumOne(nickname, albumName,requestDTO))
                .thenReturn(dummyList);

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(nickname, albumName, requestDTO);

        assertNotNull(response, "응답은 null이 아니어야 합니다.");
        assertFalse(response.getDtoList().isEmpty(), "앨범 검색 결과가 있어야 합니다.");
    }

    @Test
    void testGetAlbumOneNotFound() {
        String nickname = "artistX";
        String albumName = "없는앨범";
        RequestDTO requestDTO = new RequestDTO();

        when(albumMusicRepository.albumOne(nickname, albumName,requestDTO))
                .thenReturn(Optional.of(Page.empty()));

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(nickname, albumName, requestDTO);

        // then
        assertNull(response.getDtoList(), "검색 결과 리스트는 null이어야 합니다.");
    }

    @Test
    public void testGetSoundOne_Success() {
        // given
        int userId = 2;
        int soundId = 15;
        MusicDTO dummyMusicDTO = new MusicDTO();
        SearchTotalResultDTO dummyResult = new SearchTotalResultDTO();
        dummyResult.setMusicDTO(dummyMusicDTO);

        when(musicRepository.soundOne(userId, soundId))
                .thenReturn(Optional.of(dummyResult));

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(userId, soundId);

        // then
        assertNotNull(response.getDto(), "음원 검색 결과가 있어야 합니다.");
    }

    @Test
    public void testGetSoundOne_NotFound() {
        // given
        int userId = 9999;
        int soundId = 8888;

        when(musicRepository.soundOne(userId, soundId))
                .thenReturn(Optional.empty());

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(userId, soundId);

        // then
        assertNull(response.getDto(), "음원 검색 결과가 없어야 합니다.");
    }

    @Test
    public void testGetAlbumOne_Success() {
        int userId = 2;
        int albumId = 10;
        RequestDTO requestDTO = new RequestDTO();
        SearchTotalResultDTO dummyResult = new SearchTotalResultDTO();
        Page<SearchTotalResultDTO> page = new PageImpl<>(List.of(dummyResult));
        Optional<Page<SearchTotalResultDTO>> dummyList = Optional.of(page);

        when(albumMusicRepository.albumOne(userId, albumId,requestDTO))
                .thenReturn(dummyList);

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(userId, albumId, requestDTO);

        assertNotNull(response.getDtoList(), "앨범 검색 결과가 있어야 합니다.");
    }

    @Test
    public void testGetAlbumOne_NotFound() {
        int userId = 9999;
        int albumId = 8888;
        RequestDTO requestDTO = new RequestDTO();

        when(albumMusicRepository.albumOne(userId, albumId,requestDTO))
                .thenReturn(Optional.of(Page.empty()));

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(userId, albumId, requestDTO);

        assertEquals(0,response.getDtoList().size(), "앨범 검색 결과 리스트는 null이어야 합니다.");
    }

    @Test
    public void saveSound() {
        int userId = 2;

        // 입력용 DTO 생성
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setAlbumArtPath("test-image");
        albumDTO.setAlbumName("test-albumName");
        albumDTO.setDescription("test-albumDescription");

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setFilePath("test-music");
        musicDTO.setDescription("test-musicDescription");
        musicDTO.setTitle("test-title");

        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(Arrays.asList("piano"));
        tagsDTO.setMood(List.of("chill"));
        tagsDTO.setGenre(List.of("pop"));

        // userRepository 모킹: userId에 해당하는 사용자가 존재한다고 가정
        User dummyUser = User.builder().userId(userId).nickname("test-nickname").build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(dummyUser));

        // Act: 서비스 메소드 호출
        ResponseDTO responseDTO = soundsService.createSound(userId, albumDTO, musicDTO, tagsDTO);

        // Assert
        assertEquals("정상적으로 등록했습니다.", responseDTO.getMessage());
    }

    @Test
    public void saveInstrumentTag() {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(List.of("hyperpianotest"));
        tagsDTO.setMood(Collections.emptyList());
        tagsDTO.setGenre(Collections.emptyList());

        // 세터 없이 생성할 수 있도록 생성자 또는 빌더 사용
        InstrumentTag savedTag = InstrumentTag.builder()
                .instrumentTagName("hyperpianotest")
                .build();

        when(instrumentTagRepository.findAll())
                .thenReturn(List.of(savedTag));

        ResponseDTO response = tagsService.createTag(tagsDTO);

        // 이후 repository에서 태그를 조회한 결과가 올바른지 검증
        List<InstrumentTag> instrumentTags = instrumentTagRepository.findAll();
        boolean containsTag = instrumentTags.stream()
                .anyMatch(tag -> "hyperpianotest".equalsIgnoreCase(tag.getInstrumentTagName()));
        assertTrue(containsTag, "태그에 'hyperpianotest'가 있어야 합니다.");
    }


    // 저장 - 신규 무드 태그
    @Test
    public void saveMoodTag() {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setMood(List.of("hypermoodtest"));
        tagsDTO.setInstrument(Collections.emptyList());
        tagsDTO.setGenre(Collections.emptyList());

        MoodTag savedMoodTag = MoodTag.builder().moodTagName("hypermoodtest").build();

        when(moodTagRepository.findAll()).thenReturn(List.of(savedMoodTag));

        ResponseDTO response = tagsService.createTag(tagsDTO);

        List<MoodTag> moodTags = moodTagRepository.findAll();
        boolean containsTag = moodTags.stream()
                .anyMatch(tag -> "hypermoodtest".equalsIgnoreCase(tag.getMoodTagName()));
        assertTrue(containsTag, "태그에 'hypermoodtest'가 있어야 합니다.");
    }

    // 저장 - 신규 장르 태그
    @Test
    public void saveGenreTag() {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(Collections.emptyList());
        tagsDTO.setMood(Collections.emptyList());
        tagsDTO.setGenre(List.of("hypergenretest"));

        GenreTag savedGenreTag = GenreTag.builder().genreTagName("hypergenretest").build();

        when(genreTagRepository.findAll()).thenReturn(List.of(savedGenreTag));

        ResponseDTO response = tagsService.createTag(tagsDTO);

        List<GenreTag> genreTags = genreTagRepository.findAll();
        boolean containsTag = genreTags.stream()
                .anyMatch(tag -> "hypergenretest".equalsIgnoreCase(tag.getGenreTagName()));
        assertTrue(containsTag, "태그에 'hypergenretest'가 있어야 합니다.");
    }

    @Test
    void deleteAlbum() {
        int albumId = 10;

        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        soundsService.deleteAlbum(albumId);

        Optional<Album> album = albumRepository.findById(albumId);
        assertFalse(album.isPresent(), "앨범이 지워졌는가?");
    }

    @Test
    void deleteMusic() {
        int musicId = 15;

        when(musicRepository.findById(musicId)).thenReturn(Optional.empty());

        soundsService.deleteMusic(musicId);

        Optional<Music> music = musicRepository.findById(musicId);
        assertFalse(music.isPresent(), "음원이 지워졌는가?");
    }

    // 어드민
    @Test
    void updateAlbumAdmin() {
        int albumId = 10;

        AlbumDTO albumDTO = AlbumDTO.builder()
                .description("change-description")
                .albumName("change-albumName")
                .build();

        // 기존 앨범(더미) 생성
        Album mockAlbum = Album.builder()
                .albumId(albumId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .albumName("old-name")
                .albumArtPath("old-path.jpg")
                .description("old-desc")
                .build();

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(mockAlbum));

        soundsService.updateAlbumForAdmin(albumId, albumDTO);

        assertEquals("change-description", mockAlbum.getDescription(), "앨범 설명이 업데이트되어야 합니다.");
        assertEquals("change-albumName", mockAlbum.getAlbumName(), "앨범 이름이 업데이트되어야 합니다.");
    }

    // 사용자


    @Test
    void updateMusicForAdmin() {
        int musicId = 15;

        MusicDTO musicDTO = MusicDTO.builder()
                .title("test-title")
                .description("test-description")
                .build();

        Music mockMusic = Music.builder()
                .musicId(musicId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .title("old-title")
                .filePath("old-path.mp3")
                .price(1000)
                .description("old-desc")
                .soundType("mp3")
                .build();

        when(musicRepository.findById(musicId)).thenReturn(Optional.of(mockMusic));

        soundsService.updateMusicForAdmin(musicId, musicDTO);

        assertEquals("test-title", mockMusic.getTitle(), "타이틀이 업데이트 되었는가?");
        assertEquals("test-description", mockMusic.getDescription(), "음원 설명이 업데이트 되었는가?");
    }

    @Test
    void testUpdateInstrumentTagSpelling() {
        String beforeName = "piano";
        String afterName = "changetestpiano";
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setInstrument(List.of(afterName));

        InstrumentTag mockTag = InstrumentTag.builder().instrumentTagName(afterName).build();

        tagsService.updateInstrumentTagSpelling(beforeName, afterNameDTO);

        assertEquals(afterName, mockTag.getInstrumentTagName(), "태그 이름이 올바르게 업데이트되어야 합니다.");
    }

    @Test
    void updateVerifyAlbum() {
        int albumId = 10;
        Album mockAlbum = Album.builder()
                .albumId(albumId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .albumName("old-name")
                .albumArtPath("old-path.jpg")
                .description("old-desc")
                .build();


        when(albumRepository.findById(albumId)).thenReturn(Optional.of(mockAlbum));

        soundsService.updateVerifyAlbum(albumId);

        assertEquals(1, mockAlbum.getVerify(), "verify가 잘 허가 되었는가?");
    }

//     어드민
    @Test
    void updateAlbumForArtist_success() {
        int albumId = 10;

        AlbumDTO albumDTO = AlbumDTO.builder()
                .description("change-description")
                .albumName("change-albumName")
                .build();

        // 기존 앨범(더미) 생성
        Album mockAlbum = Album.builder()
                .albumId(albumId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .albumName("old-name")
                .albumArtPath("old-path.jpg")
                .description("old-desc")
                .build();

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(mockAlbum));

        soundsService.updateAlbumForArtist(albumId, albumDTO,1);

        assertEquals("change-description", mockAlbum.getDescription(), "앨범 설명이 업데이트되어야 합니다.");
        assertEquals("change-albumName", mockAlbum.getAlbumName(), "앨범 이름이 업데이트되어야 합니다.");
    }

    @Test
    void updateAlbumForArtist_wrong() {
        int albumId = 1000;

        AlbumDTO albumDTO = AlbumDTO.builder()
                .description("change-description")
                .albumName("change-albumName")
                .build();

        // 기존 앨범(더미) 생성 (소유자 userId=1)
        Album mockAlbum = Album.builder()
                .albumId(albumId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .albumName("old-name")
                .albumArtPath("old-path.jpg")
                .description("old-desc")
                .build();

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(mockAlbum));

        // soundsService.updateAlbumForArtist 내부에서
        // soundOwnershipCheckService.checkAlbumAccessById(1, 2)를 호출하게 되고,
        // 실제 구현체의 로직에 따라 AccessDeniedException이 발생해야 합니다.
        assertThrows(ResourceOwnershipException.class, () -> {
            soundsService.updateAlbumForArtist(albumId, albumDTO, 2);
        }, "잘못된 사용자가 앨범을 수정하려 하면 예외가 발생해야 합니다.");
    }
    // 사용자

    @Test
    void updateMusicForArtist_Success() {
        int musicId = 15;

        MusicDTO musicDTO = MusicDTO.builder()
                .title("test-title")
                .description("test-description")
                .build();

        Music mockMusic = Music.builder()
                .musicId(musicId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .title("old-title")
                .filePath("old-path.mp3")
                .price(1000)
                .description("old-desc")
                .soundType("mp3")
                .build();

        when(musicRepository.findById(musicId)).thenReturn(Optional.of(mockMusic));

        soundsService.updateMusicForAdmin(musicId, musicDTO);

        assertEquals("test-title", mockMusic.getTitle(), "타이틀이 업데이트 되었는가?");
        assertEquals("test-description", mockMusic.getDescription(), "음원 설명이 업데이트 되었는가?");
    }

    @Test
    void updateMusicForArtist_Wrong() {
        int musicId = 10;

        MusicDTO musicDTO = MusicDTO.builder()
                .title("test-title")
                .description("test-description")
                .build();

        Music mockMusic = Music.builder()
                .musicId(musicId)
                .userId(1) // 예제 사용자 ID
                .nickname("artist153")
                .title("old-title")
                .filePath("old-path.mp3")
                .price(1000)
                .description("old-desc")
                .soundType("mp3")
                .build();

        when(musicRepository.findById(musicId)).thenReturn(Optional.of(mockMusic));

        // soundsService.updateAlbumForArtist 내부에서
        // soundOwnershipCheckService.checkAlbumAccessById(1, 2)를 호출하게 되고,
        // 실제 구현체의 로직에 따라 AccessDeniedException이 발생해야 합니다.
        assertThrows(ResourceOwnershipException.class, () -> {
            soundsService.updateMusicForArtist(musicId,musicDTO, 2);
        }, "잘못된 사용자가 음원을 수정하려 하면 예외가 발생해야 합니다.");
    }
}

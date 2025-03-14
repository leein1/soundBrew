package com.soundbrew.soundbrew.sounds;

import com.soundbrew.soundbrew.domain.sound.*;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.sound.*;
import com.soundbrew.soundbrew.service.authentication.AuthenticationService;
import com.soundbrew.soundbrew.service.sound.SoundsService;
import com.soundbrew.soundbrew.service.tag.TagsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class ServiceIntegrationTest {
    //사운드
    @Autowired private SoundsService soundsService;
    @Autowired private AlbumRepository albumRepository;
    @Autowired private MusicRepository musicRepository;
    @Autowired private TagsService tagsService;
    @Autowired private InstrumentTagRepository instrumentTagRepository;
    @Autowired private MoodTagRepository moodTagRepository;
    @Autowired private GenreTagRepository genreTagRepository;
    @Autowired private MusicInstrumentTagRepository musicInstrumentTagRepository;
    @Autowired private MusicMoodTagRepository musicMoodTagRepository;
    @Autowired private MusicGenreTagRepository musicGenreTagRepository;
    @Autowired private AuthenticationService authenticationService;


    // 읽기 ** 레포지토리의 성격상 DB와 상호작용이 필요하고 -> 상호작용으로 인해서 통합테스트의 성격을 띄게 된다. -> service의 코드와 비슷해진다 -> 고로 서비스에서 읽기는 간단하게만 체크한다.

    @Test
    void testTotalSoundSearchSuccess() {
        // given
        RequestDTO requestDTO = new RequestDTO();

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalSoundSearch(requestDTO);

        // then
        assertNotNull(response);
        assertFalse(response.getDtoList().isEmpty(), "전체 검색이 잘 되었는가");
    }

    @Test
    void testTotalSoundSearchEmpty() {
        // given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("없는검색어");
        requestDTO.setType("t");

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalSoundSearch(requestDTO);

        // then
        assertNotNull(response);
        assertTrue(response.getDtoList().isEmpty(), "검색 결과가 없는가?"); // 검색 결과가 없어야 함
    }

    @Test
    void testTotalAlbumSearchSuccess() {
        // given
        RequestDTO requestDTO = new RequestDTO();

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalAlbumSearch(requestDTO);

        // then
        assertNotNull(response);
        assertFalse(response.getDtoList().isEmpty(), "전체 앨범 검색이 잘 되는가?"); // 검색 결과가 있어야 함
    }

    @Test
    void testTotalAlbumSearchSuccess2() {
        // given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("없는 앨범 키워드");
        requestDTO.setType("t");

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.totalAlbumSearch(requestDTO);

        // then
        assertNotNull(response);
        assertTrue(response.getDtoList().isEmpty(), "앨범 검색 결과가 없는가?"); // 검색 결과가 있어야 함
    }

    @Test
    void testGetSoundOneSuccess() {
        // given
        String nickname = "mele";
        String title = "도서관";

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(nickname, title);

        // then
        assertEquals(title, response.getDto().getMusicDTO().getTitle(), "검색한 음원 하나를 잘 가져왔는가?");
    }

    @Test
    void testGetSoundOneNotFound() {
        // given
        String nickname = "artistX";
        String title = "없는음원";

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(nickname, title);

        // then
        assertNull(response.getDto(), "음원 한개 검색 결과가 없는가?");
    }

    @Test
    void testGetAlbumOneSuccess() {
        // given
        String nickname = "mele";
        String albumName = "도서관 앨범";
        RequestDTO requestDTO = new RequestDTO();

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(nickname, albumName, requestDTO);

        // then
        assertNotNull(response);
        assertFalse(response.getDtoList().isEmpty(),"앨범 한개 검색 결과가 잘 나왔는가?");
    }

    @Test
    void testGetAlbumOneNotFound() {
        // given
        String nickname = "artistX";
        String albumName = "없는앨범";
        RequestDTO requestDTO = new RequestDTO();

        // when
        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(nickname, albumName, requestDTO);

        // then
        assertNull(response.getDtoList(), "앨범 한개 검색 결과가 없는가?");
    }

    @Test
    public void testGetSoundOne_Success() {
        int userId = 2;
        int soundId = 15; // 해당 음원의 id

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(userId, soundId);

        assertNotNull(response.getDto(), "음원 검색 결과가 있는가?");
    }

    @Test
    public void testGetSoundOne_NotFound() {
        int userId = 9999;
        int soundId = 8888;

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOne(userId, soundId);

        assertNull(response.getDto(), "음원 검색 결과가 없는가?");
    }

    @Test
    public void testGetAlbumOne_Success() {
        int userId = 2;
        int albumId = 15;
        RequestDTO requestDTO = new RequestDTO();

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(userId, albumId, requestDTO);

        assertNotNull(response.getDtoList(), "앨범 한개 검색 결과가 있는가?");
    }

    @Test
    public void testGetAlbumOne_NotFound() {
        int userId = 9999;
        int albumId = 8888;
        RequestDTO requestDTO = new RequestDTO();

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumOne(userId, albumId, requestDTO);

        assertEquals(0,response.getDtoList().size(), "앨범 검색 결과 리스트는 비어있어야 합니다.");
    }

    @Test
    public void testGetSoundMe_Success() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("mele");
        requestDTO.setType("n");

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundMe(requestDTO);

        assertNotNull(response.getDtoList(),"음원 검색 결과가 있는가?");
    }

    @Test
    public void testGetSoundMe_Empty() {
        // given: 조건에 맞지 않는 requestDTO
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("nonexistentSound");
        requestDTO.setType("t");

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundMe(requestDTO);

        assertNull(response.getDto(), "음원 검색 결과가 없는가?");
    }

    @Test
    public void testGetAlbumMe_Success() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("mele");
        requestDTO.setType("n");

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumMe(requestDTO);

        assertFalse(response.getDtoList().isEmpty(), "앨범 검색 결과가 있는가?");
    }

    @Test
    public void testGetAlbumMe_Empty() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("nonexistentAlbum");
        requestDTO.setType("n");

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getAlbumMe(requestDTO);

        assertTrue(response.getDtoList().isEmpty(), "앨범 검색 결과가 없는가?");
    }

    @Test
    public void testReadVerifyAlbum_WithResults() {
        // given
        RequestDTO requestDTO = new RequestDTO();

        ResponseDTO<SearchTotalResultDTO> response = soundsService.readVerifyAlbum(requestDTO);

        assertNotNull(response.getDtoList(), "검색 결과가 있는가?");
    }

    @Test
    public void testReadVerifyAlbumOne_WithResults() {
        int userId = 2;
        int albumId = 15;
        RequestDTO requestDTO = new RequestDTO();

        ResponseDTO<SearchTotalResultDTO> response = soundsService.readVerifyAlbumOne(userId, albumId, requestDTO);

        assertNotNull(response.getDtoList(), "검색 결과가 있는가?");
    }

    @Test
    public void testReadVerifyAlbumOne_NoResults() {
        // given
        int userId = 9999;    // 존재하지 않는 유저 ID 또는 조건에 맞지 않는 값
        int albumId = 8888;   // 존재하지 않는 앨범 ID
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("nonexistent");

        ResponseDTO<SearchTotalResultDTO> response = soundsService.readVerifyAlbumOne(userId, albumId, requestDTO);

        assertNull(response.getDtoList(), "검색 결과가 없는가?");
    }

    @Test
    public void testGetSoundOneForAdmin_Success() {
        int userId = 2;  // 테스트 데이터 기준 유저 ID
        int soundId = 15;  // 테스트 데이터 기준 음원 ID

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOneForAdmin(userId, soundId);

        assertNotNull(response.getDto(), "음원 검색 결과가 있는가?");
    }

    @Test
    public void testGetSoundOneForAdmin_NotFound() {
        int userId = 9999;   // 존재하지 않는 유저 ID
        int soundId = 8888;    // 존재하지 않는 음원 ID

        ResponseDTO<SearchTotalResultDTO> response = soundsService.getSoundOneForAdmin(userId, soundId);

        assertNull(response.getDto(), "음원 검색 결과가 없는가?");
    }

    // 저장 - 신규 음원
    @Test
    public void saveSound(){
        int userId = 2;

        AlbumDTO albumDTO=new AlbumDTO();
        albumDTO.setAlbumArtPath("test-image");
        albumDTO.setAlbumName("test-albumName");
        albumDTO.setDescription("test-albumDescription");

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setFilePath("test-music");
        musicDTO.setDescription("test-musicDescription");
        musicDTO.setTitle("test-title");

        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(Arrays.asList("piano"));
        tagsDTO.setMood(Arrays.asList("chill"));
        tagsDTO.setGenre(Arrays.asList("pop"));

        SoundCreateDTO soundCreateDTO = new SoundCreateDTO();
        soundCreateDTO.setAlbumDTO(albumDTO);
        soundCreateDTO.setMusicDTO(musicDTO);
        soundCreateDTO.setTagsDTO(tagsDTO);

        soundsService.createSound(userId,albumDTO,musicDTO,tagsDTO);

        assertNotNull(soundCreateDTO.getAlbumDTO(),"저장이 정상적으로 되는가?");
        assertNotNull(soundCreateDTO.getMusicDTO(),"저장이 정상적으로 되는가?");
        assertNotNull(soundCreateDTO.getTagsDTO(),"저장이 정상적으로 되는가?");
        assertEquals("test-albumName", soundCreateDTO.getAlbumDTO().getAlbumName(),"저장이 정상적으로 되는가?");
        assertEquals("test-title", soundCreateDTO.getMusicDTO().getTitle(),"저장이 정상적으로 되는가?");
        assertTrue(soundCreateDTO.getTagsDTO().getInstrument().contains("piano"),"저장이 정상적으로 되는가?");
    }

    @Test
    public void saveSound2(){
        int userId = 2;
        AlbumDTO albumDTO=new AlbumDTO();
        MusicDTO musicDTO = new MusicDTO();
        TagsDTO tagsDTO = new TagsDTO();

        SoundCreateDTO soundCreateDTO = new SoundCreateDTO();
        soundCreateDTO.setAlbumDTO(albumDTO);
        soundCreateDTO.setMusicDTO(musicDTO);
        soundCreateDTO.setTagsDTO(tagsDTO);

        try {
            soundsService.createSound(userId, albumDTO, musicDTO, tagsDTO);
            fail("예상한 예외가 발생하지 않았습니다.");
        } catch (DataIntegrityViolationException e) {
            assertNotNull(e.getMessage());
        }
    }

    // 저장 - 신규 인스트루먼트 태그
    @Test
    public void saveInstrumentTag() {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(List.of("hyperpianotest"));
        tagsDTO.setMood(Collections.emptyList());
        tagsDTO.setGenre(Collections.emptyList());

        ResponseDTO response = tagsService.createTag(tagsDTO);

        List<InstrumentTag> instrumentTags = instrumentTagRepository.findAll();
        boolean containsPiano = instrumentTags.stream().anyMatch(tag -> "hyperpianotest".equalsIgnoreCase(tag.getInstrumentTagName()));
        assertTrue(containsPiano, " 태그에 'hyperpianotest'가 있어야 합니다.");
    }

    // 저장 - 신규 무드 태그
    @Test
    public void saveMoodTag(){
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setMood(List.of("hypermoodtest"));
        tagsDTO.setInstrument(Collections.emptyList());
        tagsDTO.setGenre(Collections.emptyList());

        ResponseDTO response = tagsService.createTag(tagsDTO);

        List<MoodTag> moodTags = moodTagRepository.findAll();
        boolean containsPiano = moodTags.stream().anyMatch(tag -> "hypermoodtest".equalsIgnoreCase(tag.getMoodTagName()));
        assertTrue(containsPiano, " 태그에 'hypermoodtest'가 있어야 합니다.");
    }

    // 저장 - 신규 장르 태그
    @Test
    public void saveGenreTag(){
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(Collections.emptyList());
        tagsDTO.setMood(Collections.emptyList());
        tagsDTO.setGenre(List.of("hypergenretest"));

        ResponseDTO response = tagsService.createTag(tagsDTO);

        List<GenreTag> genreTag = genreTagRepository.findAll();
        boolean containsPiano = genreTag.stream().anyMatch(tag -> "hypergenretest".equalsIgnoreCase(tag.getGenreTagName()));
        assertTrue(containsPiano, " 태그에 'hypergenretest'가 있어야 합니다.");
    }

    // 삭제 - sounds
    @Test
    void deleteAlbum(){
        int albumId = 15;

        soundsService.deleteAlbum(albumId);

        Optional<Album> album = albumRepository.findById(albumId);

        assertFalse(album.isPresent(), "앨범이 지워졌는가?");
    }

    @Test
    void deleteMusic(){
        int musicId = 15;

        soundsService.deleteMusic(musicId);

        Optional<Music> music = musicRepository.findById(musicId);

        assertFalse(music.isPresent(), "음원이 지워졌는가?");
    }

    // 업데이트 - album
    @Test
    void updateAlbumForAdmin(){
        int albumId = 15;
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setDescription("change-description");
        albumDTO.setAlbumName("change-albumName");

        soundsService.updateAlbumForAdmin(albumId,albumDTO);

        Optional<Album> updatedAlbum = albumRepository.findById(albumId);  // 가정: albumRepository가 존재한다고 가정

        assertTrue(updatedAlbum.isPresent(), "앨범이 존재해야 합니다.");
        assertEquals("change-description", updatedAlbum.get().getDescription(), "앨범 설명이 업데이트되어야 합니다.");
        assertEquals("change-albumName", updatedAlbum.get().getAlbumName(), "앨범 이름이 업데이트되어야 합니다.");
    }

    // 업데이트 - music
    @Test
    void updateMusicForAdmin(){
        int musicId = 13;
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setTitle("test-title");
        musicDTO.setDescription("test-description");

        soundsService.updateMusicForAdmin(musicId,musicDTO);

        Optional<Music> updatedMusic = musicRepository.findById(musicId);

        assertTrue(updatedMusic.isPresent(), "음원이 존재하는가?");
        assertEquals("test-title", updatedMusic.get().getTitle(),"타이틀이 업데이트 되었는가?");
        assertEquals("test-description", updatedMusic.get().getDescription(),"음원 설명이 업데이트 되었는가?");
    }

    // 업데이트 - soundsTagsLink
    @Test
    public void testUpdateLinkTagsForAdmin_Success() {
        int musicId = 15;

        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(Arrays.asList("piano"));  // 예: "piano" 태그만 연결
        tagsDTO.setMood(Arrays.asList("chill"));         // 예: "chill" 태그만 연결
        tagsDTO.setGenre(Arrays.asList("pop"));           // "pop" 태그는 그대로

        ResponseDTO response = tagsService.updateLinkTagsForAdmin(musicId, tagsDTO);

        List<MusicInstrumentTag> instrumentTags = musicInstrumentTagRepository.findByIdMusicId(musicId);
        assertNotNull(instrumentTags, "Instrument 태그 리스트는 null이 아니어야 합니다.");
        assertEquals(1, instrumentTags.size(), "연결된 Instrument 태그 수는 1개여야 합니다.");
        assertEquals("piano", instrumentTags.get(0).getInstrumentTag().getInstrumentTagName(), "Instrument 태그 이름이 'piano'여야 합니다.");

        List<MusicMoodTag> moodTags = musicMoodTagRepository.findByIdMusicId(musicId);
        assertNotNull(moodTags, "Mood 태그 리스트는 null이 아니어야 합니다.");
        assertEquals(1, moodTags.size(), "연결된 Mood 태그 수는 1개여야 합니다.");
        assertEquals("chill", moodTags.get(0).getMoodTag().getMoodTagName(), "Mood 태그 이름이 'chill'여야 합니다.");

        List<MusicGenreTag> genreTags = musicGenreTagRepository.findByIdMusicId(musicId);
        assertNotNull(genreTags, "Genre 태그 리스트는 null이 아니어야 합니다.");
        assertEquals(1, genreTags.size(), "연결된 Genre 태그 수는 1개여야 합니다.");
        assertEquals("pop", genreTags.get(0).getGenreTag().getGenreTagName(), "Genre 태그 이름이 'pop'이어야 합니다.");
    }

    @Test
    public void testUpdateInstrumentTagSpelling() {
        String beforeName = "piano";
        String afterName = "changetestpiano";
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setInstrument(List.of(afterName));

        ResponseDTO response = tagsService.updateInstrumentTagSpelling(beforeName, afterNameDTO);

        Optional<InstrumentTag> updatedTag = instrumentTagRepository.findByInstrumentTagName(afterName);
        assertNotNull(updatedTag, "업데이트 후 태그가 존재해야 합니다.");
        assertEquals(afterName, updatedTag.get().getInstrumentTagName(), "태그 이름이 올바르게 업데이트되어야 합니다.");
    }

    @Test
    public void testUpdateMoodTagSpelling() {
        String beforeName = "chill";
        String afterName = "changetestchill";
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setMood(List.of(afterName));

        ResponseDTO response = tagsService.updateMoodTagSpelling(beforeName, afterNameDTO);

        Optional<MoodTag> updatedTag = moodTagRepository.findByMoodTagName(afterName);
        assertNotNull(updatedTag, "업데이트 후 태그가 존재해야 합니다.");
        assertEquals(afterName, updatedTag.get().getMoodTagName(), "태그 이름이 올바르게 업데이트되어야 합니다.");
    }

    @Test
    public void testUpdateGenreTagSpelling() {
        String beforeName = "country";
        String afterName = "edmtestcjange";
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setGenre(List.of(afterName));

        ResponseDTO response = tagsService.updateGenreTagSpelling(beforeName, afterNameDTO);

        Optional<GenreTag> updatedTag = genreTagRepository.findByGenreTagName(afterName);
        assertNotNull(updatedTag, "업데이트 후 태그가 존재해야 합니다.");
        assertEquals(afterName, updatedTag.get().getGenreTagName(), "태그 이름이 올바르게 업데이트되어야 합니다.");
    }

    //업데이트 - updateVerifyAlbum
    @Test
    void updateVerifyAlbum(){
        int albumId = 15;

        soundsService.updateVerifyAlbum(albumId);

        Optional<Album> album = albumRepository.findById(albumId);

        assertEquals(1, album.get().getVerify(),"verify가 잘 허가 되었는가?");
    }

    @Test
    void updateAlbumForArtist_Success(){
        int albumId = 15;
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setDescription("change-description");
        albumDTO.setAlbumName("change-albumName");

        soundsService.updateAlbumForArtist(albumId,albumDTO, 2);

        Optional<Album> updatedAlbum = albumRepository.findById(albumId);  // 가정: albumRepository가 존재한다고 가정

        assertTrue(updatedAlbum.isPresent(), "앨범이 존재해야 합니다.");
        assertEquals("change-description", updatedAlbum.get().getDescription(), "앨범 설명이 업데이트되어야 합니다.");
        assertEquals("change-albumName", updatedAlbum.get().getAlbumName(), "앨범 이름이 업데이트되어야 합니다.");
    }

    @Test
    void updateAlbumForArtist_Bad(){
        int albumId = 15;
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setDescription("change-description");
        albumDTO.setAlbumName("change-albumName");

        assertThrows(ResourceOwnershipException.class, () -> {
            soundsService.updateAlbumForArtist(albumId,albumDTO, 99);
        }, "잘못된 사용자가 음원을 수정하려 하면 예외가 발생해야 합니다.");
    }

    // 업데이트 - music
    @Test
    void updateMusicForArtist_Bad(){
        int musicId = 13;
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setTitle("test-title");
        musicDTO.setDescription("test-description");

        assertThrows(ResourceOwnershipException.class, () -> {
            soundsService.updateMusicForArtist(musicId,musicDTO, 9999);
        }, "잘못된 사용자가 음원을 수정하려 하면 예외가 발생해야 합니다.");
    }
    @Test
    void updateMusicForArtist_Success(){
        int musicId = 13;
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setTitle("test-title");
        musicDTO.setDescription("test-description");

        soundsService.updateMusicForArtist(musicId,musicDTO,2);

        Optional<Music> updatedMusic = musicRepository.findById(musicId);

        assertTrue(updatedMusic.isPresent(), "음원이 존재하는가?");
        assertEquals("test-title", updatedMusic.get().getTitle(),"타이틀이 업데이트 되었는가?");
        assertEquals("test-description", updatedMusic.get().getDescription(),"음원 설명이 업데이트 되었는가?");
    }

}

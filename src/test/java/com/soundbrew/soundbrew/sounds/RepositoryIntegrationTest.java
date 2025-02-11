package com.soundbrew.soundbrew.sounds;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.sound.SearchTotalResultDTO;
import com.soundbrew.soundbrew.repository.sound.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RepositoryIntegrationTest {
    //사운드
    @Autowired private AlbumMusicRepository albumMusicRepository;
    @Autowired private MusicRepository musicRepository;

    // 읽기 - get all tags
    @Test
    public void readAllTags(){
        //given
        RequestDTO requestDTO = new RequestDTO();

        //when
        Optional<List<SearchTotalResultDTO>> expect = musicRepository.getAllTags(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(),"태그가 잘 들고와지는가?");
    }

    @Test
    public void readAllTags2(){
        //given
        Map<String, String> more = new HashMap<>();
        more.put("instrument", "harp");
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setMore(more);

        //when
        Optional<List<SearchTotalResultDTO>> expect = musicRepository.getAllTags(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(),"악기를 검색조건으로 포함한 태그가 잘 들고와지는가?");
    }

    @Test
    public void readAllTags3(){
        //given
        Map<String, String> more = new HashMap<>();
        more.put("instrument", "W!R!O!N!G");
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setMore(more);

        //when
        Optional<List<SearchTotalResultDTO>> expect = musicRepository.getAllTags(requestDTO);

        //then
        assertTrue(expect.get().isEmpty(),"잘못된 태그 조건을 포함했을 때, 태그가 비어있는가?");
    }

    // 읽기 - soundOne
    @Test
    public void readSoundOne(){
        //given
        String nickname = "mele";
        String title = "도서관";

        //when
        Optional<SearchTotalResultDTO> expect = musicRepository.soundOne(nickname,title);

        //then
        assertNotNull(expect.get().getMusicDTO().getDescription(), "SoundOne - String 잘 검색했는가?");
    }

    // 읽기 - soundOne(int)
    @Test
    public void readSoundOneId(){
        int userId =2;
        int musicId = 10;

        Optional<SearchTotalResultDTO> expect = musicRepository.soundOne(userId,musicId);

        assertNotNull(expect.get().getMusicDTO().getDescription(), "SoundOne - int 잘 검색했는가?");
    }

    // 읽기 - Search
    @Test
    public void readSearch(){
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("mele");
        requestDTO.setType("n");

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.search(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(), "유저 키워드를 포함한 Search는 잘 작동하는가");
    }

    @Test
    public void readSearch2(){
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("도서관");
        requestDTO.setType("t");

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.search(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(), "앨범 타이틀 키워드를 포함한 Search는 잘 작동하는가");
    }

    @Test
    public void readSearch3(){
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("!$@#$");
        requestDTO.setType("t");

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.search(requestDTO);

        //then
        assertEquals(0, expect.get().getTotalElements(), "잘못된 조건을 포함한 Search는 잘 거르는가");
    }

    @Test
    public void readSearch4(){
        //given
        RequestDTO requestDTO = new RequestDTO();

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.search(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(), "모든 Search는 잘 가져오는가");
    }

    // 읽기 - SearchAlbum
    @Test
    public void readSearchAlbum(){
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("mele");
        requestDTO.setType("n");

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.searchAlbum(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(), "유저 키워드를 포함한 SearchAlbum는 잘 작동하는가");
    }

    @Test
    public void readSearchAlbum2(){
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("도서관 앨범");
        requestDTO.setType("t");

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.searchAlbum(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(), "타이틀 키워드를 포함한 Search는 잘 작동하는가");
    }

    @Test
    public void readSearchAlbum3(){
        //given
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setKeyword("!@#$");
        requestDTO.setType("t");

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.searchAlbum(requestDTO);

        //then
        assertEquals(0, expect.get().getTotalElements(), "잘못된 키워드를 포함한 Search는 잘 거르는가");
    }

    @Test
    public void readSearchAlbum4(){
        //given
        RequestDTO requestDTO = new RequestDTO();

        //when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.searchAlbum(requestDTO);

        //then
        assertFalse(expect.get().isEmpty(), "모든 SearchAlbum는 잘 가져오는가");
    }

    // 읽기 - albumOne
    @Test
    public void readAlbumOne(){
        String albumName ="도서관 앨범";
        String nickname = "mele";
        RequestDTO requestDTO = new RequestDTO();

        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.albumOne(nickname,albumName,requestDTO);

        assertNotEquals(0, expect.get().getTotalElements(), "albumOne - String 잘 검색했는가?");
    }

    // 읽기 - albumOne(int)
    @Test
    public void readAlbumOneId(){
        int albumId =10;
        int userId = 2;
        RequestDTO requestDTO = new RequestDTO();

        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.albumOne(userId,albumId,requestDTO);

        assertNotEquals(0, expect.get().getTotalElements(), "albumOne - int 잘 검색했는가?");
    }

    // 읽기 - verifyAlbum
    @Test
    public void readVerifyAlbum(){
        // given
        RequestDTO requestDTO = new RequestDTO();

        // when
        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.verifyAlbum(requestDTO);

        // then
        assertNotEquals(0, expect.get().getTotalElements(), "verify 앨범이 잘 검색되는가 ");
    }

    // 읽기 - verifyAlbumOne
    @Test
    public void readVerifyAlbumOne(){
        int albumId = 10;
        int userId = 2;
        RequestDTO requestDTO = new RequestDTO();

        Optional<Page<SearchTotalResultDTO>> expect = albumMusicRepository.verifyAlbumOne(userId,albumId,requestDTO);

        assertNotEquals(0, expect.get().getTotalElements(), "verifyOne 앨범이 잘 검색되는가 ");
    }
}

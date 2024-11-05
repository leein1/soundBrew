package com.soundbrew.soundbrew.service.sound;

import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.repository.UserRepository;
import com.soundbrew.soundbrew.service.util.SoundProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SoundSearchService {
    @Autowired
    private AlbumMusicService albumMusicService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private InstrumentTagService instrumentTagService;
    @Autowired
    private MoodTagService moodTagService;
    @Autowired
    private GenreTagService genreTagService;

    @Autowired
    private SoundProcessor soundProcessor;

    public SoundSearchFilterDto soundSearch(SoundSearchRequestDto soundSearchRequestDto, Pageable pageable){
        List<SoundSearchResultDto> before = albumMusicService.readTotal(soundSearchRequestDto,pageable);

        // 태그들을 배열화 해서 화면 상단 태그버튼 보이게끔 생성
        SoundSearchFilterDto after = soundProcessor.replaceTagsToArray(before);
        // 태그에 ","를 빼서 이쁘게 보이기
        List<SoundSearchResultDto> afterSearch = soundProcessor.replaceCommaWithSpace(before);

        // 프로세싱한 두개 하나로 합쳐서 보내기
        after.setSoundSearchResultDto(afterSearch);
        return after;
    }

    // for manager
    public List<AlbumDto> readAlbumByArtistName(String nickName){
        int userid = userRepository.findByNickname(nickName).orElseThrow();

        return albumService.readAlbumWithUserId(userid);
    }

    // for manager
    public List<MusicDto> readMusicByArtistName(String nickName){
        int userid = userRepository.findByNickname(nickName).orElseThrow();

        return musicService.readMusicWithUserId(userid);
    }

    // for admin
    public AlbumDto readAlbum(){
        return albumService.readAlbum();
    }

    // for admin
    public List<MusicDto> readMusic(){ return musicService.readMusic(); }

    // for admin (tag #1)
    public MoodTagDto readMoodTag(){ return moodTagService.readMoodTag(); }
    // for admin (tag #2)
    public GenreTagDto readGenreTag(){ return genreTagService.readGenreTag();}
    // for admin (tag #3)
    public InstrumentTagDto readInstTag(){ return instrumentTagService.readInstrumentTag();}
}

package com.soundbrew.soundbrew.service.authentication;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class SoundOwnershipCheckService {
    public void checkAlbumAccessById(int albumUserId, int givenUserId) {
        if(albumUserId != givenUserId) throw new AccessDeniedException("해당 앨범에 접근할 권한이 없습니다.");
    }
    public void checkMusicAccessById(int musicUserId, int givenUserId) {
        if(musicUserId != givenUserId) throw new AccessDeniedException("해당 음원에 접근할 권한이 없습니다.");
    }
    public void checkAlbumAccessByNickname(String albumNickname, String givenNickname) {
        if(albumNickname.equals(givenNickname)) throw new AccessDeniedException("해당 앨범에 접근할 권한이 없습니다.");
    }
    public void checkMusicAccessByNickname(String musicNickname, String givenNickname) {
        if(musicNickname.equals(givenNickname)) throw new AccessDeniedException("해당 음원에 접근할 권한이 없습니다.");
    }
}

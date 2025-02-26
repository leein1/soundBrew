package com.soundbrew.soundbrew.service.authentication;

import com.soundbrew.soundbrew.handler.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class SoundOwnershipCheckService {
    public void checkAlbumAccessById(int albumUserId, int givenUserId) {
        if(albumUserId != givenUserId) throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);
    }
    public void checkMusicAccessById(int musicUserId, int givenUserId) {
        if(musicUserId != givenUserId) throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);
    }
    public void checkAlbumAccessByNickname(String albumNickname, String givenNickname) {
        if(albumNickname.equals(givenNickname))  throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);
    }
    public void checkMusicAccessByNickname(String musicNickname, String givenNickname) {
        if(musicNickname.equals(givenNickname))  throw new BusinessException(BusinessException.BUSINESS_ERROR.RESOURCE_NOT_ACCESS);
    }
}

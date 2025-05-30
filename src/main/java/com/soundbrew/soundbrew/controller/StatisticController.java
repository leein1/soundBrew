package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundMyStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundTotalStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.tag.TagsTotalStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.user.UserStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.usersubscription.SubscriptionTotalStatisticDTO;
import com.soundbrew.soundbrew.service.authentication.AuthenticationService;
import com.soundbrew.soundbrew.service.sound.SoundsStatisticService;
import com.soundbrew.soundbrew.service.tag.TagsStatisticService;
import com.soundbrew.soundbrew.service.user.UserStatisticService;
import com.soundbrew.soundbrew.service.userSubscription.UserSubscriptionStatisticService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final SoundsStatisticService soundsStatisticService;
    private final TagsStatisticService tagsStatisticService;
    private final UserStatisticService userStatisticService;
    private final UserSubscriptionStatisticService userSubscriptionStatisticService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sounds/stats")
    public ResponseEntity<ResponseDTO<SoundTotalStatisticDTO>> getSoundStats(@ModelAttribute RequestDTO requestDTO) {
        ResponseDTO<SoundTotalStatisticDTO> response = soundsStatisticService.getSoundStats(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sounds/tracks")
    public ResponseEntity<ResponseDTO<SoundStatisticDTO>> getBestSellingTrack(@ModelAttribute RequestDTO requestDTO) {
        ResponseDTO<SoundStatisticDTO> response = soundsStatisticService.getBestSellingTrack(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sounds/artists")
    public ResponseEntity<ResponseDTO<SoundStatisticDTO>> getBestSellingArtist(@ModelAttribute RequestDTO requestDTO) {
        ResponseDTO<SoundStatisticDTO> response = soundsStatisticService.getBestSellingArtist(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sounds/uploads")
    public ResponseEntity<ResponseDTO<SoundStatisticDTO>> getBestUploadArtist(@ModelAttribute RequestDTO requestDTO) {
        ResponseDTO<SoundStatisticDTO> response = soundsStatisticService.getBestUploadArtist(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tags/stats")
    public ResponseEntity<ResponseDTO<TagsTotalStatisticDTO>> getBestTags(Authentication authentication){
        ResponseDTO<TagsTotalStatisticDTO> response = tagsStatisticService.getTagsWithTopUsage();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/stats")
    public ResponseEntity<ResponseDTO<UserStatisticDTO>> getUsersStatsForAdmin(Authentication authentication) {
        ResponseDTO<UserStatisticDTO> response = userStatisticService.getUsersStatsForAdmin();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/subscription/stats")
    public ResponseEntity<ResponseDTO<SubscriptionTotalStatisticDTO>> getUserSubscriptionStatsForAdmin(Authentication authentication) {
        ResponseDTO<SubscriptionTotalStatisticDTO> response = userSubscriptionStatisticService.getUserSubscriptionStatsForAdmin();
        return ResponseEntity.ok().body(response);
    }

    // 나의 통계
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sounds/stats/me")
    public ResponseEntity<ResponseDTO<SoundMyStatisticDTO>> getMySoundsStats(Authentication authentication){
        int userId = authenticationService.getUserId(authentication);
        ResponseDTO<SoundMyStatisticDTO> response =soundsStatisticService.getMySoundStats(userId);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tags/stats/me")
    public ResponseEntity<ResponseDTO<TagsTotalStatisticDTO>> getMyTagsStats(Authentication authentication){
        int userId= authenticationService.getUserId(authentication);
        ResponseDTO<TagsTotalStatisticDTO> response = tagsStatisticService.getTagsWithTopUsageByUserId(userId);

        return ResponseEntity.ok().body(response);
    }

//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/subscription/stats/me")
//    public ResponseEntity<ResponseDTO<SubscriptionTotalStatisticDTO>> getMySubscriptionStats() {
//        //        int userId = authenticationService.getUserId(authentication);
//        ResponseDTO<SubscriptionTotalStatisticDTO> response = userSubscriptionStatisticService.getUserSubscriptionStatsForAdmin();
//        return ResponseEntity.ok().body(response);
//    }
}

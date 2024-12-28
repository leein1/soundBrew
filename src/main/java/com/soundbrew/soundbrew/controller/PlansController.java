package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.domain.Subscription;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.SubscriptionDTO;
import com.soundbrew.soundbrew.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
@Log4j2
public class PlansController {

    private final SubscriptionService subscriptionService;

//    @ApiOperation(value = "plans GET", notes = "GET 방식으로 모든 구독제 정보 가져오기")
    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO<SubscriptionDTO>> SearchAll(){



        ResponseDTO<SubscriptionDTO> responseDTO = subscriptionService.findAllSubscriptions();

        log.info("responseDTO in controller : {}", responseDTO);

        return ResponseEntity.ok().body(responseDTO);
    }

}

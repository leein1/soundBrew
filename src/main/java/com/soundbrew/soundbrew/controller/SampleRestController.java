package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDTO;
import com.soundbrew.soundbrew.service.SoundsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class SampleRestController {
    private final SoundsService soundsService;

}

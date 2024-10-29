package com.soundbrew.soundbrew.controller.sound;

import com.soundbrew.soundbrew.service.sound.SoundManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SoundManageController {
    @Autowired
    private SoundManagerService soundManagerService;



}

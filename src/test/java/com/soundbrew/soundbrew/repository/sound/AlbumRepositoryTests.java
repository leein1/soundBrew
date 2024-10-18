package com.soundbrew.soundbrew.repository.sound;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AlbumRepositoryTests {

    @Autowired
    private AlbumRepository albumRepository;


}

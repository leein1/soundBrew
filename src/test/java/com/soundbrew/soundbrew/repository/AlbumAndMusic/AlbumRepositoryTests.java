package com.soundbrew.soundbrew.repository.AlbumAndMusic;

import com.soundbrew.soundbrew.domain.Album;
import com.soundbrew.soundbrew.repository.AlbumRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AlbumRepositoryTests {

    @Autowired
    private AlbumRepository albumRepository;


}

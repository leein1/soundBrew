package com.soundbrew.soundbrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SoundBrewApplication {

    public static void main(String[] args) {

        SpringApplication.run(SoundBrewApplication.class, args);
    }

}

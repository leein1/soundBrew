package com.soundbrew.soundbrew;

import com.soundbrew.soundbrew.config.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
=======
>>>>>>> feature/kyoung
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
<<<<<<< HEAD
@EnableConfigurationProperties(FileProperties.class)
=======
>>>>>>> feature/kyoung
public class SoundBrewApplication {

    public static void main(String[] args) {

        SpringApplication.run(SoundBrewApplication.class, args);
    }

}

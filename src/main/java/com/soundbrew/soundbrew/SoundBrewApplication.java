package com.soundbrew.soundbrew;

import com.soundbrew.soundbrew.config.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(FileProperties.class)
public class SoundBrewApplication {

    public static void main(String[] args) {

        SpringApplication.run(SoundBrewApplication.class, args);
    }

}

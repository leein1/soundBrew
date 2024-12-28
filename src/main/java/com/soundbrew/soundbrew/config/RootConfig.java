package com.soundbrew.soundbrew.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(false)  // 리플렉션 비활성화
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}
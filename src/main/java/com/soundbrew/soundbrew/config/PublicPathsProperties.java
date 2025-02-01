package com.soundbrew.soundbrew.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "public")
@Getter
@Setter
public class PublicPathsProperties {

    private List<String> paths;


}

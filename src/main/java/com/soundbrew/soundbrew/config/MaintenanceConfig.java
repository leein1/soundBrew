package com.soundbrew.soundbrew.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "maintenance")
@Getter
@Setter
public class MaintenanceConfig {

    private boolean mode;
}

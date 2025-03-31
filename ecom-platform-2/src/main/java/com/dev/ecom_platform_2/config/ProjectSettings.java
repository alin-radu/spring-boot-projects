package com.dev.ecom_platform_2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "project.settings")
public class ProjectSettings {
    private Image image = new Image();

    @Getter
    @Setter
    public static class Image {
        private String path;
        private String maxSize;
        private List<String> allowedTypes;
    }
}

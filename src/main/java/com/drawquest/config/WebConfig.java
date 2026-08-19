package com.drawquest.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties(UploadProperties.class)
public class WebConfig implements WebMvcConfigurer {

    private final UploadProperties uploadProperties;

    public WebConfig(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String storageLocation = Path.of(uploadProperties.getDrawingsDir())
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        if (!storageLocation.endsWith("/")) {
            storageLocation += "/";
        }

        registry.addResourceHandler(uploadProperties.getDrawingsPublicPath() + "/**")
                .addResourceLocations(storageLocation);
    }
}

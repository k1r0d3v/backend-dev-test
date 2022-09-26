package com.example.backenddevtest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ApplicationWebMvcConfigurer implements WebMvcConfigurer {
    @Value("${application.enable.publicAPI:false}")
    private Boolean publicAPI;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        if (publicAPI) {
            corsRegistry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOrigins("*")
                    .allowedHeaders("*");
        }
    }
}
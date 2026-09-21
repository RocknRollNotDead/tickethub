package ru.codeportfolio.tickethub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableResilientMethods
public class CorsConfiguration implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry r) {
        r.addMapping("/**").allowedOrigins("*").allowedMethods("*");
    }
}
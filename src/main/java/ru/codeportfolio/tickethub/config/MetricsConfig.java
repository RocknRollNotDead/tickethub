package ru.codeportfolio.tickethub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class MetricsConfig {
    @Bean
    AtomicLong bookingCounter(){
        return new AtomicLong(0);
    }
    @Bean
    AtomicLong transferCounter(){
        return new AtomicLong(0);
    }

}

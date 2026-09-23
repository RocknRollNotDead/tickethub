package ru.codeportfolio.tickethub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
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

    @Bean
    Queue<String> auditLog() {
        return new ConcurrentLinkedQueue<>();
    }

}

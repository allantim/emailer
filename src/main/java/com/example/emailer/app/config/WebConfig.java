package com.example.emailer.app.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class WebConfig {

    // TODO add to application.yml
    // TODO look into whether we want to use something like Client Http Request Factory
    // As I believes this has a connection pool
    private static final Duration CONNECT_TIMEOUT = Duration.ofMillis(1000);
    private static final Duration READ_TIMEOUT = Duration.ofMillis(3000);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setReadTimeout(READ_TIMEOUT)
            .build();
    }
}

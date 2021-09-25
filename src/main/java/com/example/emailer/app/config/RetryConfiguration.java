package com.example.emailer.app.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class RetryConfiguration {
    // TODO CREATE A PROPERTIES CLASS TO LOAD DATA FROM APPLICATION.YML
    @Bean
    public Retry sendEmailRetry(@Value("${app.email-sender-retry.maxRetryAttempts}") int maxAttempts,
                                @Value("${app.email-sender-retry.waitDurationMillis}") long waitDurationMillis) {
        return Retry.of("send-email-retry",
            RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(Duration.of(waitDurationMillis, ChronoUnit.MILLIS))
                .build());
    }
}

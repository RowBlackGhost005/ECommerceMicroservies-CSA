package com.marin.ECommerce.API.Gateway.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterService {
    private final ConcurrentHashMap<String, RateLimiter> clientLimiters = new ConcurrentHashMap<>();

    @Bean
    public RateLimiter getRateLimiter(String clientId) {
        return clientLimiters.computeIfAbsent(clientId, key ->
                RateLimiter.of(key, RateLimiterConfig.custom()
                        .limitRefreshPeriod(Duration.ofMinutes(1))
                        .limitForPeriod(5) // 5 requests per minute per client
                        .timeoutDuration(Duration.ofMillis(1000))
                        .build()));
    }
}

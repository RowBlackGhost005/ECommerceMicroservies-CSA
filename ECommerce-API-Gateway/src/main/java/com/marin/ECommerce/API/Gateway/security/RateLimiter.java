package com.marin.ECommerce.API.Gateway.security;

import com.marin.ECommerce.API.Gateway.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimiter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiterService = new RateLimiterService();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String clientId = request.getRemoteAddr();

        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = rateLimiterService.getRateLimiter(clientId);
        if (rateLimiter.acquirePermission()) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
    }
}
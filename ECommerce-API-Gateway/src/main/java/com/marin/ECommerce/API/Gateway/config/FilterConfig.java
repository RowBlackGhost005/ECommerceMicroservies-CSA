package com.marin.ECommerce.API.Gateway.config;

import com.marin.ECommerce.API.Gateway.security.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimiter> rateLimiterFilter() {
        FilterRegistrationBean<RateLimiter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimiter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}

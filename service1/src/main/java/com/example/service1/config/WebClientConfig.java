package com.example.service1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service2.base-url}")
    private String service2BaseUrl;

    @Bean
    public WebClient service2WebClient() {
        return WebClient.builder()
                .baseUrl(service2BaseUrl)
                .build();
    }
}
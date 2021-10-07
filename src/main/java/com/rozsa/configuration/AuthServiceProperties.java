package com.rozsa.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service")
public class AuthServiceProperties {

    @Value("${service.auth.cookie}")
    private String authCookieName;
}

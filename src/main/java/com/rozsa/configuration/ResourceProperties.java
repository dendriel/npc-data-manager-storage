package com.rozsa.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "resource")
public class ResourceProperties {
    @Value("${resource.access.expiration}")
    private Long expirationTimeInSec;
}

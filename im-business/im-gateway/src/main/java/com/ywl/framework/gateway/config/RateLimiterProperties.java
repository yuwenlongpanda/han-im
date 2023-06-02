package com.ywl.framework.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * @author liao
 */
@Data
@Validated
@Component
@ConfigurationProperties(prefix = RateLimiterProperties.PREFIX)
public class RateLimiterProperties {

    public static final String PREFIX = "spring.cloud.gateway.rate-limit";

    private FilterDefinition filter;

    private Set<String> whiteList;
}

package com.ywl.framework.gateway.filter;

import com.ywl.framework.gateway.config.RateLimiterProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author liao
 */
public class IpKeyResolver implements KeyResolver {

    private final RateLimiterProperties rateLimiterProperties;

    public IpKeyResolver(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        String hostName = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName();
        if (rateLimiterProperties.getWhiteList().contains(hostName)) {
            return Mono.just("____EMPTY_KEY__");
        }
        return Mono.just(hostName);
    }
}

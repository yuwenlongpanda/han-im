package com.ywl.framework.gateway.config;


import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.ywl.framework.gateway.filter.IpKeyResolver;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;
import java.util.List;

/**
 * @author liao
 */
@Configuration
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    private final RateLimiterProperties rateLimiterProperties;

    private final GatewayProperties gatewayProperties;


    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer, RateLimiterProperties rateLimiterProperties, GatewayProperties gatewayProperties) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
        this.rateLimiterProperties = rateLimiterProperties;
        this.gatewayProperties = gatewayProperties;
    }

    /**
     * 跨域设置
     * 注：仅开发环境使用，测试、生产环境已经使用nginx处理跨域
     */
    @Bean
    @Profile("dev")
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    /**
     * 限流异常处理器
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 限流过滤器
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }


    @Bean("ipResolver")
    @Primary
    public IpKeyResolver initIpKeyResolver() {
        if (rateLimiterProperties.getFilter() == null) {
            return new IpKeyResolver(rateLimiterProperties);
        }
        List<RouteDefinition> routes = gatewayProperties.getRoutes();
        routes.forEach(routeDefinition -> {
            List<FilterDefinition> filters = routeDefinition.getFilters();
            filters.add(rateLimiterProperties.getFilter());
        });
        return new IpKeyResolver(rateLimiterProperties);
    }
}

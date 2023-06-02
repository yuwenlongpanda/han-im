package com.ywl.framework.gateway.filter;

import com.ywl.framework.gateway.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



/**
 * 登录用户的JWT转化成用户信息
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest sourceRequest = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = sourceRequest.getPath().toString();
        String realIp = IpUtils.getRealIp(sourceRequest);
        log.info("请求地址={} 来源Ip={}", path, realIp);
        HttpHeaders responseHeaders = response.getHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}

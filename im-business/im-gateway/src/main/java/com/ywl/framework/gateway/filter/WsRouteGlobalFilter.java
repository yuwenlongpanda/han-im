package com.ywl.framework.gateway.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ywl.framework.redis.RedisClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * websocket路由转发规则
 */
@Slf4j
@Component
public class WsRouteGlobalFilter implements GlobalFilter, Ordered {
    private static final String WS_PREFIX = "/ws";
    private static final String IM_NETTY_NAME = "horse-im-netty";
    private static final String IM_DEFAULT_URI = "ws://horse-im-netty";
    private static final String WS_PROTOCOL = "ws://";
    private static final String NETTY_SERVER_ZSET_KEY = "im:netty:server";

    @Autowired
    private RedisClient redisClient;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String path = exchange.getRequest().getPath().toString();
        Route oldRoute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (path.startsWith(WS_PREFIX)) { // websocket 连接请求
            String address = redisClient.zGet(NETTY_SERVER_ZSET_KEY, 0);
            log.info("分配的websocket服务器地址 {}", address);
            if (address == null) {
                ServerHttpResponse response = exchange.getResponse();
                return response.writeWith(Flux.just(response.bufferFactory().wrap(createWsError())));
            }
            // 构建新的ws uri
            String newPath = WS_PROTOCOL + address + path;
            newPath = addParams(newPath, req);
            URI newUri = URI.create(newPath);
            log.info("需要连接的websocket服务器地址 {}", newUri);
            ServerHttpRequest request = req.mutate().uri(newUri).build();

            Route newRoute = Route.async()
                    .asyncPredicate(oldRoute.getPredicate())
                    .filters(oldRoute.getFilters())
                    .id(oldRoute.getId())
                    .order(oldRoute.getOrder())
                    .uri(newUri)
                    .build();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, newRoute);
            return chain.filter(exchange.mutate().request(request).build());
        }
        return chain.filter(exchange);
    }

    private String addParams(String path, ServerHttpRequest request) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        StringJoiner stringJoiner = new StringJoiner("&");
        Set<Map.Entry<String, List<String>>> entries = queryParams.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            stringJoiner.add(key + "=" + value.get(0));
        }
        try {
            String encode = URLEncoder.encode(stringJoiner.toString(), "UTF-8");
            return path + "?" + encode;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    @Override
    public int getOrder() {
        return 1;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route(IM_NETTY_NAME,
                r -> r.path(WS_PREFIX)
                        .uri(IM_DEFAULT_URI)).build();
        return routes.build();
    }

    private final Gson gson = new GsonBuilder().create();

    public byte[] createWsError() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", 500);
        map.put("message", "websocket连接异常");
        return gson.toJson(map).getBytes();
    }
}

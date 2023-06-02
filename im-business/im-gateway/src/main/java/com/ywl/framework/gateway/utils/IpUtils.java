package com.ywl.framework.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Objects;

/**
 * IP 工具类
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
public class IpUtils {

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String UNKNOWN = "unknown";
    private static final String SPLIT = ",";


    /**
     * 获取请求的真实IP
     *
     * @param request
     * @return
     */
    public static String getRealIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst(X_FORWARDED_FOR);
        log.info("x-forwarded-for ip {}", headers.get(X_FORWARDED_FOR));
        log.info("X-Real-IP ip {}", headers.get(X_REAL_IP));
        if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
            log.debug("多次反向代理后会有多个ip值，第一个ip才是真实ip: {}", ip);
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            boolean contains = ip.contains(SPLIT);
            if (contains) {
                ip = ip.split(",")[0];
            }
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(PROXY_CLIENT_IP);
            log.info("PROXY_CLIENT_IP: {}", ip);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(WL_PROXY_CLIENT_IP);
            log.info("WL_PROXY_CLIENT_IP: {}", ip);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(HTTP_CLIENT_IP);
            log.info("HTTP_CLIENT_IP: {}", ip);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(HTTP_X_FORWARDED_FOR);
            log.info("HTTP_X_FORWARDED_FOR: {}", ip);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(X_REAL_IP);
            log.info("X_REAL_IP: {}", ip);
        }
        if (checkIp(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        return ip;
    }

    private static boolean checkIp(String ip) {
        return ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip);
    }
}

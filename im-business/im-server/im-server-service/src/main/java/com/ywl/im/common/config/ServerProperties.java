package com.ywl.im.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "im.server")
public class ServerProperties implements ImProperties {

    /**
     * ip和端口号分割符
     */
    public static final String ADDRESS_SPLIT = ":";

    /**
     * ws协议
     */
    public static final String WS_PROTOCOL = "ws://";

    /**
     * 服务名字
     */
    public String nacosNettyName = "im-netty";

    /**
     * websocket连接用户标识可以
     */
    public String clientParamKey = "client";

    /**
     * websocket连接类型标识
     */
    public String clientTypeKey = "type";

    /**
     * 定时获取netty服务列表的间隔
     */
    public int pullNewNettySeconds = 3;

    /**
     * websocket中发送的消息大小限制 3kb
     */
    public int maxContentLength = 1024 * 3;

    /**
     * ws path
     */
    public String webSocketPrefix = "/ws";

    /**
     * socket 缓冲队列大小
     */
    public int soBackLog = 1024;

    /**
     * server读空闲时间 超过该时间的channel关闭
     */
    public int serverReaderIdleTimeSeconds = 20;

    /**
     * client读空闲时间 超过该时间的channel关闭
     */
    public int clientReaderIdleTimeSeconds = 20;

    /**
     * 客户端续期时间
     */
    public int clientRenewalSeconds = 3600;

    /**
     * 服务器端口
     */
    public int port = 8080;

    /**
     * 服务器客户端连接事件线程数
     */
    public int bossNthreads = 1;

    /**
     * 服务器处理客户端读写事件线程数
     */
    public int workerNthreads = 2;
}

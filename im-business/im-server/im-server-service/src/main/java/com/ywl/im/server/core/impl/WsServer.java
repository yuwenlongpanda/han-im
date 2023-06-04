package com.ywl.im.server.core.impl;

import com.ywl.im.server.channel.WsChannelInitializer;
import com.ywl.im.server.core.ImProperties;
import com.ywl.im.server.core.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class WsServer implements Server {

    private WsChannelInitializer wsChannelInitializer;  // WebSocket通道初始化器
    private ExecutorService executorService;  // 线程池
    private ImProperties properties;  // 服务器配置属性

    private static volatile boolean isStart;  // 标记服务器是否已启动
    private final Object lock = new Object();  // 锁对象，用于同步控制
    private String ip;  // 服务器IP地址
    private int port;  // 服务器监听的端口号
    private NioEventLoopGroup boos;  // Boss线程组，用于处理连接请求
    private NioEventLoopGroup worker;  // Worker线程组，用于处理网络IO事件
    private Channel channel;  // 服务器通道，用于与客户端进行数据交互

    public WsServer(WsChannelInitializer wsChannelInitializer, DefaultImProperties properties, ExecutorService executorService) throws UnknownHostException {
        this.wsChannelInitializer = wsChannelInitializer;
        this.properties = properties;
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.port = properties.getPort();
        this.boos = new NioEventLoopGroup(properties.getBossNthreads());
        this.worker = new NioEventLoopGroup(properties.getWorkerNthreads());
        this.executorService = executorService;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void start() {
        synchronized (lock) {
            if (isStart()) {
                log.error("重复启动 {}", this);
                return;  // 如果服务器已经启动，则直接返回，避免重复启动
            }

            try {
                ServerBootstrap bootstrap = new ServerBootstrap();  // 创建服务器启动引导类实例

                // 配置服务器启动引导类
                bootstrap.group(boos, worker)
                        .channel(NioServerSocketChannel.class)  // 使用 NIO 传输通道
                        .childHandler(wsChannelInitializer)  // 设置管道初始化器
                        .option(ChannelOption.SO_BACKLOG, properties.getSoBackLog())  // 设置服务器的 socket 参数
                        .childOption(ChannelOption.SO_KEEPALIVE, true);  // 设置子通道的 socket 参数

                // 绑定服务器端口并启动服务器
                ChannelFuture channelFuture = bootstrap.bind(port).sync();
                channel = channelFuture.channel();  // 获取服务器通道
                this.ip = InetAddress.getLocalHost().getHostAddress();  // 获取服务器的 IP 地址
                log.info("服务器 {}:{} 启动", getIp(), getPort());
                isStart = true;  // 标记服务器已启动

                // 监听服务器通道关闭事件
                channel.closeFuture().addListener((ChannelFutureListener) channelFuture1 -> {
                    log.error("服务器关闭...");
                    shutdown();  // 执行服务器关闭操作
                });
            } catch (Exception e) {
                log.error("启动服务器时出现异常", e);
                shutdown();  // 出现异常时执行服务器关闭操作
            }
        }
    }

    @Override
    public void shutdown() {
        if (boos != null) {
            boos.shutdownGracefully();
        }

        if (worker != null) {
            worker.shutdownGracefully();
        }

        if (executorService != null) {
            executorService.shutdown();
        }

        isStart = false;
    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
    public String toString() {
        return "WsServer{" + "port=" + port + ", ip='" + ip + '\'' + '}';
    }

}

package com.ywl.im.server;

import com.ywl.im.common.config.IServer;
import com.ywl.im.common.config.ImProperties;
import com.ywl.im.common.config.ServerProperties;
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
public class WsServer implements IServer {

    private ChannelInit channelInit;
    private ExecutorService executorService;
    private ImProperties serverProperties;

    private static volatile boolean isStart;
    private final Object lock = new Object();
    private String ip;
    private int port;
    private NioEventLoopGroup boos;
    private NioEventLoopGroup worker;
    private Channel channel;


    public WsServer(ChannelInit channelInit, ServerProperties properties, ExecutorService executorService) throws UnknownHostException {
        this.channelInit = channelInit;
        this.serverProperties = properties;
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
            try {
                if (isStart()) {
                    log.error("重复启动 {}", this);
                    return;
                }

                ServerBootstrap b = new ServerBootstrap();
                b.group(boos, worker)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(channelInit)
                        .option(ChannelOption.SO_BACKLOG, serverProperties.getSoBackLog())
                        .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
                ChannelFuture channelFuture = b.bind(port).sync();
                channel = channelFuture.channel();
                this.ip = InetAddress.getLocalHost().getHostAddress();
                log.info("服务器 {}:{} 启动", getIp(), getPort());
                isStart = true;

                channel.closeFuture().addListener((ChannelFutureListener) channelFuture1 -> {
                    shutdown();
                    log.error("服务器关闭...");
                    System.exit(1);
                });
            } catch (Exception e) {
                shutdown();
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
        return "WsServer{" +
                "port=" + port +
                ", ip='" + ip + '\'' +
                '}';
    }

}

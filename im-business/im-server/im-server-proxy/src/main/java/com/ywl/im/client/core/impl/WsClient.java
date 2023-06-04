package com.ywl.im.client.core.impl;

import com.ywl.framework.common.callback.ImCallback;
import com.ywl.im.client.core.Client;
import com.ywl.im.client.handler.netty.HandshakeHandler;
import com.ywl.im.client.handler.netty.HeartHandler;
import com.ywl.im.client.handler.netty.LifecycleHandler;
import com.ywl.im.client.handler.netty.MessageHandler;
import com.ywl.im.client.utils.NetworkAddressUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket客户端
 * 用于与WebSocket服务器建立连接并发送消息
 * 实现了Client接口
 */
@Slf4j
public class WsClient implements Client {
    private final EventLoopGroup group = new NioEventLoopGroup(2);
    private final MessageHandler messageHandler;
    private final String serverIp;
    private final int serverPort;
    private final String url;
    private Channel channel;
    private volatile boolean serverIsClosed = false;
    private volatile boolean serverIsClosing = false;

    public WsClient(String serverIp, int serverPort, String url, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.url = url;
    }

    /**
     * 获取服务器端口
     *
     * @return 服务器端口
     */
    @Override
    public int getServerPort() {
        return serverPort;
    }

    /**
     * 获取服务器IP地址
     *
     * @return 服务器IP地址
     */
    @Override
    public String getServerIp() {
        return serverIp;
    }

    /**
     * 打开与Netty服务器的连接
     *
     * @return 打开连接是否成功
     */
    @Override
    public boolean open(ImCallback callback) {
        try {
            WsClient current = this;
            HandshakeHandler handshakeHandler = new HandshakeHandler(url);  // 创建握手处理器

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(createChannelInitializer(current, handshakeHandler));

            // 连接到服务器并进行握手
            channel = bootstrap.connect(new InetSocketAddress(serverIp, serverPort))
                    .sync()
                    .channel();
            handshakeHandler.handshakeFuture().sync();  // 等待握手完成
            log.info("握手完成 客户端 {}:{} 连接上了服务器 {}:{} ", NetworkAddressUtil.getLocalIp(channel), NetworkAddressUtil.getLocalPort(channel), NetworkAddressUtil.getRemoteIp(channel), NetworkAddressUtil.getRemotePort(channel));
            if (callback != null) {
                callback.onSuccess();
            }

            // 在连接关闭时执行关闭操作
            channel.closeFuture().addListener((ChannelFutureListener) channelFuture -> close());

            // 循环发送心跳消息
            while (!serverIsClosed) {
                channel.writeAndFlush(new PingWebSocketFrame());  // 发送心跳消息
                try {
                    TimeUnit.SECONDS.sleep(10);  // 间隔5秒发送心跳消息
                } catch (InterruptedException e) {
                    // 忽略中断异常
                }
            }
        } catch (Exception e) {
            // 连接失败 释放当前正在连接的服务器
            log.error("连接服务器失败 {}", e.getMessage());
        } finally {
            close();
        }
        return false;
    }

    /**
     * 创建ChannelInitializer并添加处理器
     */
    private ChannelInitializer<NioSocketChannel> createChannelInitializer(WsClient client, HandshakeHandler handshakeHandler) {
        return new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
                ch.pipeline()
                        .addLast(new IdleStateHandler(0, 600, 0))  // 添加空闲状态处理器，用于处理连接空闲事件
                        .addLast(new HeartHandler(client))  // 添加心跳处理器，用于发送心跳消息
                        .addLast(new LifecycleHandler(client))  // 添加生命周期处理器，处理连接的生命周期事件
                        .addLast(new HttpClientCodec())  // 添加HTTP客户端编解码器，用于编解码HTTP请求和响应
                        .addLast(new HttpObjectAggregator(524288))  // 添加HTTP对象聚合器，用于将HTTP请求或响应的多个部分合并为完整的消息
                        .addLast(handshakeHandler)  // 添加握手处理器，用于执行WebSocket握手
                        .addLast(messageHandler);  // 添加消息处理器，用于处理服务器响应的消息
            }
        };
    }

    /**
     * 关闭客户端连接
     */
    @Override
    public void close() {
        if (serverIsClosing || serverIsClosed) {
            return;
        }
        serverIsClosing = true;

        log.info("客户端关闭连接 channel={}", channel);

        // removeConnectServer() 方法被注释掉，不执行删除服务器连接的逻辑

        group.shutdownGracefully();
        serverIsClosed = true;
        serverIsClosing = false;
    }

//    /**
//     * 从服务器中移除客户端连接
//     */
//    public void removeConnectServer() {
//        ServerAsClientAble ofServer = getOfServer();
//        ofServer.removeAsClient(serverIp, serverPort);
//        RedisService redisService = ImServerContext.getRedisService();
//        if (redisService == null) {
//            log.warn("redisService为空");
//            return;
//        }
//        String address = NetworkAddressUtil.address(serverIp, serverPort);
//        log.warn("从redis移除连接的服务器 {}", address);
//        redisService.removeNettyServer(address);
//    }

    /**
     * 发送消息给WebSocket服务器
     *
     * @param content 消息内容
     */
    @Override
    public void sendMessage(String content) {
        if (!channel.isActive()) {
            close();
            return;
        }
        channel.writeAndFlush(new TextWebSocketFrame(content));
    }

    /**
     * 获取WebSocket客户端的字符串表示形式
     *
     * @return WebSocket客户端的字符串表示形式
     */
    @Override
    public String toString() {
        return "WebSocketClient{" + "port=" + serverPort + ", ip='" + serverIp + '\'' + ", channel=" + channel +
                ", url='" + url + '\'' + '}';
    }
}


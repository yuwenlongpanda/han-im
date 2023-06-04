package com.ywl.im.client.handler.netty;

import com.ywl.im.client.core.impl.WsClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * ws 连接通用处理器 处理通道的断开
 */
@Slf4j
public class LifecycleHandler extends ChannelInboundHandlerAdapter {

    private final WsClient client;

    public LifecycleHandler(WsClient client) {
        this.client = client;
    }

    /**
     * 关闭连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.warn("客户端断开连接 移除连接的服务器 关闭通道{}", ctx.channel());
        client.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端连接发生错误 {} {}", ctx.channel(), cause.getMessage());
    }

}

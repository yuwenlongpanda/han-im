package com.ywl.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WebSocket在建立连接、关闭连接和发生异常时触发相应的事件和回调。
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class LifecycleHandler extends ChannelInboundHandlerAdapter {

    /**
     * 建立连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("建立链接 {}", ctx.channel());
    }

    /**
     * 关闭连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("断开连接 {}", ctx.channel());
    }

    /**
     * 异常时触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.debug("{} 已经异常断开 异常是{}", ctx.channel(), cause.getMessage());
    }

}

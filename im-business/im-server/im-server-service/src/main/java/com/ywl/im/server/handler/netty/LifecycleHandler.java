package com.ywl.im.server.handler.netty;

import com.ywl.im.server.handler.UserChannelHandler;
import com.ywl.im.server.utils.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocket在建立连接、关闭连接和发生异常时触发相应的事件和回调。
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class LifecycleHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private UserChannelHandler userChannelHandler;

    /**
     * 建立连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("建立链接 {}", ctx.channel());
        super.channelActive(ctx);
    }

    /**
     * 关闭连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接 {}", ctx.channel());
        super.channelInactive(ctx);
    }

    /**
     * 关闭连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        try {
            Channel channel = ctx.channel();
            log.info("断开连接 {}", channel);

            // 从各个管理器中移除通道
            ChannelUtil.removeChannel(channel);
            userChannelHandler.removeChannel(channel);
        } catch (Exception e) {
            log.error("关闭连接发生错误", e);
            ctx.close();
        }
    }


    /**
     * 异常时触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{} 已经异常断开 异常是{}", ctx.channel(), cause.getMessage());
    }

}

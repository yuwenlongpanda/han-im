package com.ywl.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * websocket服务 连接通用处理器 处理通道的断开
 *
 * @author: zhou miao
 * @date 2022/04/09
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class WsServerCommonHandler extends ChannelInboundHandlerAdapter {
//    @Resource
//    private UserChannelManager userChannelManager;
//    @Resource
//    private ChannelManager channelManager;
//    @Resource
//    private StoreWaiterChannelManager storeWaiterChannelManager;

    /**
     * 关闭连接
     *
     * @param ctx 通道上下文
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        try {
            log.info("断开连接 {}", ctx.channel());
//            channelManager.remove(ctx.channel());
//            userChannelManager.removeChannel(ctx.channel());
//            storeWaiterChannelManager.removeChannel(ctx.channel());
        } catch (Exception e) {
            log.error("==== {}", e.getStackTrace());
            log.error("端口连接发生错误 {}", e.getMessage());
            ctx.close();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}

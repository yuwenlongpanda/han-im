package com.ywl.im.client.handler.netty;

import com.ywl.im.client.core.impl.WsClient;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartHandler extends ChannelDuplexHandler {
    private final WsClient client;

    public HeartHandler(WsClient client) {
        this.client = client;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 删除无用通道
                log.warn("未及时发送心跳 客户端移除已经连接的服务器 同时关闭连接 {}", ctx.channel());
                client.close();
                return;
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}

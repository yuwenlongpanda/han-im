package com.ywl.im.client.handler.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WebSocket客户端消息处理器
 * 用于处理从服务器接收到的消息
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理从服务器接收到的消息
     *
     * @param ctx Channel处理上下文
     * @param msg 接收到的消息对象
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof TextWebSocketFrame) {
            // 如果是TextWebSocketFrame类型的消息
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            log.info("收到服务器消息 {} {}", ctx.channel(), textFrame.text());
        } else if (msg instanceof CloseWebSocketFrame) {
            // 如果是CloseWebSocketFrame类型的消息
            log.info("连接收到关闭帧 {}", ctx.channel());
            ctx.channel().close();
        } else if (msg instanceof PongWebSocketFrame) {
            // 如果是PongWebSocketFrame类型的消息，表示心跳响应
            log.info("收到服务器心跳响应 {} {}", ctx.channel(), msg);
        }
    }

}
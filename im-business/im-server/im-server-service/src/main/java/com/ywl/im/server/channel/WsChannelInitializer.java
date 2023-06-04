package com.ywl.im.server.channel;

import com.ywl.im.server.core.impl.DefaultImProperties;
import com.ywl.im.server.handler.netty.HandshakeHandler;
import com.ywl.im.server.handler.netty.HeartHandler;
import com.ywl.im.server.handler.netty.LifecycleHandler;
import com.ywl.im.server.handler.netty.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WsChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Resource
    private HeartHandler heartHandler;
    @Resource
    private LifecycleHandler lifecycleHandler;
    @Resource
    private HandshakeHandler handshakeHandler;
    @Resource
    private MessageHandler messageHandler;
    @Resource
    private DefaultImProperties defaultImProperties;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // 心跳处理
        pipeline.addLast(new IdleStateHandler(defaultImProperties.getServerReaderIdleTimeSeconds(), 0, 0));
        pipeline.addLast(heartHandler);
        // 关闭或者异常处理
        pipeline.addLast(lifecycleHandler);

        //以下三个是Http的支持
        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //http聚合器
        pipeline.addLast(new HttpObjectAggregator(defaultImProperties.getMaxContentLength()));
        //支持写大数据流
        pipeline.addLast(new ChunkedWriteHandler());

        // 握手处理
        pipeline.addLast(handshakeHandler);

        // websocket支持,设置路由
        // 默认情况下，WebSocketServerProtocolHandler会处理关闭帧（CloseFrame）和Ping帧（PingFrame），并自动发送相应的Pong帧（PongFrame）
        // WebSocketServerProtocolHandler会自动处理WebSocket的握手和帧的发送与接收，而你的自定义Handler可以专注于处理WebSocket消息的业务逻辑
        pipeline.addLast(new WebSocketServerProtocolHandler(defaultImProperties.getWebSocketPrefix(), true));

        // 消息处理
        // 在自定义Handler中，通过判断消息类型来区分处理逻辑。WebSocket消息可以分为文本消息和二进制消息两种类型
        pipeline.addLast(messageHandler);
    }

}

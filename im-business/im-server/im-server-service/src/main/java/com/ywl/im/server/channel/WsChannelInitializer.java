package com.ywl.im.server.channel;

import com.ywl.im.server.core.impl.DefaultImProperties;
import com.ywl.im.server.handler.HandshakeHandler;
import com.ywl.im.server.handler.HeartHandler;
import com.ywl.im.server.handler.LifecycleHandler;
import com.ywl.im.server.handler.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
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
        //以下三个是Http的支持
        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //http聚合器
        pipeline.addLast(new HttpObjectAggregator(defaultImProperties.getMaxContentLength()));
        //支持写大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        // WebSocket数据压缩
        pipeline.addLast(new WebSocketServerCompressionHandler());
        //websocket支持,设置路由
        pipeline.addLast(new WebSocketServerProtocolHandler(defaultImProperties.getWebSocketPrefix(), true));

        // 心跳处理
        pipeline.addLast(new IdleStateHandler(defaultImProperties.getServerReaderIdleTimeSeconds(), 0, 0));
        pipeline.addLast(heartHandler);
        // 关闭或者异常处理
        pipeline.addLast(lifecycleHandler);
        // 握手处理
        pipeline.addLast(handshakeHandler);
        // 消息处理
        pipeline.addLast(messageHandler);
    }

}

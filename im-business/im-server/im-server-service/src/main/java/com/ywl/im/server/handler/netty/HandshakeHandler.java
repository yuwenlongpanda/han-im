package com.ywl.im.server.handler.netty;

import com.ywl.im.server.handler.UserChannelHandler;
import com.ywl.im.server.models.HandshakeParam;
import com.ywl.im.server.utils.ChannelUtil;
import com.ywl.im.server.utils.WebSocketUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 处理ws握手请求handler
 *
 * @author zhou miao
 * @date 2022/04/09
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HandshakeHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private UserChannelHandler userChannelHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            log.info("握手请求 {}", msg);
            if (!handleHandshakeSuccess((FullHttpRequest) msg, ctx)) {
                log.error("连接信息无效 {}", msg);
                ctx.close();
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    /**
     * 处理握手请求 将用户连接信息加入到Redis缓存
     *
     * @param fullHttpRequest 握手请求
     * @param ctx             通道上下文
     * @return 是否处理成功
     */
    private boolean handleHandshakeSuccess(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        try {
            Map<String, String> params = WebSocketUtil.parseHandshakeParams(fullHttpRequest);
            if (CollectionUtils.isEmpty(params)) {
                return false;
            }

            // 集群之间不需要校验和保留通道信息 直接放行
            if (WebSocketUtil.isInternalConnect(params)) {
                return true;
            }

            if (!WebSocketUtil.isCommonConnect(params)) {
                log.error("连接参数不正确 {}", params);
                return false;
            }

            HandshakeParam handshakeParam = WebSocketUtil.getHandshakeParam(params);
            ChannelUtil.addChannel(ctx.channel(), handshakeParam);

            // 连接成功后保存用户的信息和用户的通道信息
            userChannelHandler.addChannel(ctx.channel(), 1, handshakeParam.getUser());
            return true;
        } catch (Exception e) {
            log.error("握手过程中发生错误 {}", e.getMessage());
            return false;
        }
    }

}


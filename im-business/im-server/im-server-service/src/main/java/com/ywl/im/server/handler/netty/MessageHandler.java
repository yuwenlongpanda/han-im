package com.ywl.im.server.handler.netty;

import com.alibaba.fastjson.JSONObject;
import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.im.server.handler.UserChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 处理websocket中的消息
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class MessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Autowired
    private UserChannelHandler userChannelHandler;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof TextWebSocketFrame) {
            textHandler(ctx, (TextWebSocketFrame) webSocketFrame);
        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
//            binaryWebSocketFrameHandler(ctx, (BinaryWebSocketFrame)webSocketFrame);
        }
    }

    private void textHandler(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) {
        String text = textWebSocketFrame.text();
        if (StringUtils.isEmpty(text)) {
            return;
        }

        ImMqMessage imMqMessage = JSONObject.parseObject(text, ImMqMessage.class);
        if (imMqMessage != null && CollectionUtils.isNotEmpty(imMqMessage.getToIds()) && StringUtils.isNotEmpty(imMqMessage.getContent())) {
            Set<Channel> userChannels = userChannelHandler.getUserChannels(1, imMqMessage.getToIds().get(0));
            for (Channel channel : userChannels) {
                channel.writeAndFlush(new TextWebSocketFrame(imMqMessage.getContent()));
            }
        }

    }
//
//    private void binaryWebSocketFrameHandler(ChannelHandlerContext ctx, BinaryWebSocketFrame webSocketFrame) {
//        // protobuf 处理
//        ByteBuf content = webSocketFrame.content();
//        byte[] bytes = new byte[content.readableBytes()];
//        content.readBytes(bytes);
//        try {
//            ReqMessageProto.Model reqMessage = ReqMessageProto.Model.parseFrom(bytes);
//            Integer traceType = reqMessage.getTraceType();
//
//            ChannelMessageTypeEnum requestMessageTypeEnum = IDict.getByCode(ChannelMessageTypeEnum.class, traceType);
//            if (requestMessageTypeEnum == null) {
//                ctx.writeAndFlush(RspFrameUtil.createRspFrame(reqMessage.getTraceId(), reqMessage.getTraceType(), RspMessage.ERROR, "traceType错误", null));
//                return;
//            }
//            AbstractClientMessageService<?> abstractClientMessageService = getAbstractClientMessageService(requestMessageTypeEnum);
//            abstractClientMessageService.handleMessage(ctx, reqMessage);
//        } catch (InvalidProtocolBufferException e) {
//            ctx.writeAndFlush(RspFrameUtil.createRspFrame(null, null, RspMessage.ERROR, "消息错误", null));
//        }
//    }
//
//    private AbstractClientMessageService<?> getAbstractClientMessageService(ChannelMessageTypeEnum requestMessageTypeEnum) {
//        AbstractClientMessageService<?> abstractClientMessageService = messageServiceMap.get(requestMessageTypeEnum.getMsgHandleService());
//        // 本类缓存提高性能 不用去spring容器中找
//        if (abstractClientMessageService == null) {
//            synchronized (lock) {
//                abstractClientMessageService = messageServiceMap.get(requestMessageTypeEnum.getMsgHandleService());
//                if (abstractClientMessageService == null) {
//                    abstractClientMessageService = (AbstractClientMessageService<?>) applicationContext.getBean(requestMessageTypeEnum.getMsgHandleService());
//                    messageServiceMap.put(requestMessageTypeEnum.getMsgHandleService(), abstractClientMessageService);
//                }
//            }
//        }
//        return abstractClientMessageService;
//    }
//

}

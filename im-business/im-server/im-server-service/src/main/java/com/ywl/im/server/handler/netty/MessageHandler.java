package com.ywl.im.server.handler.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理websocket中的消息
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class MessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof TextWebSocketFrame) {
            System.out.println(((TextWebSocketFrame) webSocketFrame).text());
        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
//            binaryWebSocketFrameHandler(ctx, (BinaryWebSocketFrame)webSocketFrame);
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

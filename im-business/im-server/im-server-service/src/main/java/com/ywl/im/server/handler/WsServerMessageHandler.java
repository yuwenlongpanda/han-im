package com.ywl.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理websocket中的消息
 *
 * @author zhou miao
 * @date 2022/04/09
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class  WsServerMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

    }
//    @Resource
//    private UserChannelManager userChannelManager;
//    @Resource
//    private ChannelManager channelManager;
//    @Resource
//    private StoreWaiterChannelManager storeWaiterChannelManager;
//    @Resource
////    private UserConnectService userConnectService;
//    @Resource
//    private ApplicationContext applicationContext;
//
//    // 消息服务对象的引用缓存
//    private final Map<Class<?>, AbstractClientMessageService<?>> messageServiceMap = new HashMap<>();
//    private final Object lock = new Object();
//
//    @Override
//    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
//        if (webSocketFrame instanceof PingWebSocketFrame) {
//            pingWebSocketFrameHandler(ctx, (PingWebSocketFrame) webSocketFrame);
//        } else if (webSocketFrame instanceof TextWebSocketFrame) {
//            System.out.println(webSocketFrame);
//        } else if (webSocketFrame instanceof CloseWebSocketFrame) {
//            closeWebSocketFrameHandler(ctx, (CloseWebSocketFrame) webSocketFrame);
//        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
//            binaryWebSocketFrameHandler(ctx, (BinaryWebSocketFrame)webSocketFrame);
//        }
//    }
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
//    /**
//     * 客户端发送断开请求处理
//     *
//     * @param ctx 通道上下文
//     * @param frame 关闭消息体
//     */
//    private void closeWebSocketFrameHandler(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
//        log.info("{} 申请关闭 {}", ctx.channel(), frame);
//        channelManager.remove(ctx.channel());
//        userChannelManager.removeChannel(ctx.channel());
//        storeWaiterChannelManager.removeChannel(ctx.channel());
//    }
//
//    /**
//     * 处理客户端心跳包
//     *
//     * @param ctx 通道上下文
//     * @param frame 关闭消息体
//     */
//    private void pingWebSocketFrameHandler(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
//        log.info("客户端发送心跳请求 {} {}", ctx.channel(), frame);
//        ctx.channel().writeAndFlush(new PongWebSocketFrame());
//    }

}

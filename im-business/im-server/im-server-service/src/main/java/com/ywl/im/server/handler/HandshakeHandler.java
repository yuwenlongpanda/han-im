package com.ywl.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof FullHttpRequest) {
//            log.debug("握手请求 {}", msg);
//            if (!handleHandshakeSuccess((FullHttpRequest) msg, ctx)) {
//                log.error("连接信息无效 {}", msg);
//                ctx.close();
//                return;
//            }
//        }
//        super.channelRead(ctx, msg);
//    }
//
//    /**
//     * 处理握手请求 将用户连接信息加入到redis缓存
//     *
//     * @param fullHttpRequest 握手请求
//     * @param ctx             通道上下文
//     */
//    private boolean handleHandshakeSuccess(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
//        try {
//            Map<String, String> handShakeMapParam = userConnectService.handShakeMapParam(fullHttpRequest);
//            if (userConnectService.isInternalConnect(handShakeMapParam) && nettyClusterManager.isClusterMember(AddressUtil.remoteIp(ctx.channel()))) {
//                // 集群之间不需要校验和保留通道信息 直接放行
//                return true;
//            }
//            if (!userConnectService.isCommonConnect(handShakeMapParam) && !userConnectService.isWaiterConnect(handShakeMapParam)) {
//                log.error("连接参数不对 {}", handShakeMapParam);
//                return false;
//            }
//            HandshakeParam handshakeParam = userConnectService.getHandshakeParam(handShakeMapParam);
//            ThirdSystemConfig thirdSystemConfig = thirdSystemConfigService.findByClient(handshakeParam.getClient());
//            // TODO 暂时注释
////            if (invalidTicketService.checkIsUseAndUpdateTicket(thirdSystemConfig, handshakeParam.getTicket(), handshakeParam.getUser(), TicketTypeEnum.CONNECT_TICKET)) {
////                log.error("连接失败，ticket过期 {} {}", handshakeParam, thirdSystemConfig);
////                return false;
////            }
//            channelManager.add(ctx.channel(), handshakeParam);
//            String connectType = handshakeParam.getConnect();
//            ConnectTypeEnum connectTypeEnum = IDict.getByCode(ConnectTypeEnum.class, connectType);
//            switch (connectTypeEnum) {
//                case COMMON:
//                    // 连接成功后 保存用户的信息和用户的通道信息
//                    return userChannelManager.addChannel(ctx.channel(), thirdSystemConfig.getId(), handshakeParam.getUser());
//                case WAITER:
//                    StoreConfig storeConfig = storeConfigService.findBySysId(thirdSystemConfig.getId());
//                    if (storeConfig == null) {
//                        log.error("客服连接失败，商铺配置不存在 {}", thirdSystemConfig);
//                        return false;
//                    }
//                    // TODO 暂时删除
////                    StoreWaiter storeWaiter = storeWaiterService.findByStoreIdAndWaiterId(storeConfig.getId(), handshakeParam.getUser());
////                    if (storeWaiter == null) {
////                        log.error("客服连接失败，客服不存在 {}", thirdSystemConfig);
////                        return false;
////                    }
//                    return storeWaiterChannelManager.addChannel(ctx.channel(), handshakeParam.getUser(), storeConfig.getSysId(), storeConfig.getId());
//                default:
//                    log.error("连接类型错误 {}", connectType);
//                    return false;
//            }
//        } catch (Exception e) {
//            log.error("握手产生错误 {}", e.getMessage());
//            return false;
//        }
//    }


}

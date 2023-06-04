package com.ywl.im.server.utils;

import com.ywl.im.server.models.HandshakeParam;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Channel与握手参数之间的管理器，用于存储和检索握手参数。
 */
public class ChannelUtil {
    private static final Map<String, HandshakeParam> channelHandshakeMap = new ConcurrentHashMap<>();

    /**
     * 从管理器中移除通道及其对应的握手参数。
     *
     * @param channel 要移除的通道
     * @return 被移除的握手参数，如果不存在则返回null
     */
    public static HandshakeParam removeChannel(Channel channel) {
        return channelHandshakeMap.remove(getChannelId(channel));
    }

    /**
     * 向管理器中添加通道及其对应的握手参数。
     *
     * @param channel        要添加的通道
     * @param handshakeParam 要添加的握手参数
     */
    public static void addChannel(Channel channel, HandshakeParam handshakeParam) {
        channelHandshakeMap.put(getChannelId(channel), handshakeParam);
    }

    /**
     * 根据通道获取对应的握手参数。
     *
     * @param channel 要获取握手参数的通道
     * @return 对应的握手参数，如果不存在则返回null
     */
    public static HandshakeParam getHandshakeParam(Channel channel) {
        return channelHandshakeMap.get(getChannelId(channel));
    }

    /**
     * 获取通道的唯一标识符。
     *
     * @param channel 要获取标识符的通道
     * @return 通道的唯一标识符
     */
    private static String getChannelId(Channel channel) {
        return channel.id().asLongText();
    }
}

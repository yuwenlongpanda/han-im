package com.ywl.im.server.utils;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * 用于处理网络地址的实用工具类
 */
public class NetworkAddressUtil {

    private static final String ADDRESS_SPLIT = ":"; // 地址分隔符
    private static final String WS_PROTOCOL = "ws://"; // WebSocket协议前缀

    private NetworkAddressUtil() {
    }

    /**
     * 获取远程客户端的IP地址
     *
     * @param channel 客户端通道
     * @return 远程客户端的IP地址
     */
    public static String getRemoteIp(Channel channel) {
        if (channel == null || !channel.isActive()) {
            return null;
        }
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    /**
     * 获取远程客户端的端口号
     *
     * @param channel 客户端通道
     * @return 远程客户端的端口号
     */
    public static int getRemotePort(Channel channel) {
        if (channel == null) {
            return -1;
        }
        return ((InetSocketAddress) channel.remoteAddress()).getPort();
    }

    /**
     * 获取本地服务器的IP地址
     *
     * @param channel 服务器通道
     * @return 本地服务器的IP地址
     */
    public static String getLocalIp(Channel channel) {
        if (channel == null) {
            return null;
        }
        return ((InetSocketAddress) channel.localAddress()).getAddress().getHostAddress();
    }

    /**
     * 获取本地服务器的端口号
     *
     * @param channel 服务器通道
     * @return 本地服务器的端口号
     */
    public static int getLocalPort(Channel channel) {
        if (channel == null) {
            return -1;
        }
        return ((InetSocketAddress) channel.localAddress()).getPort();
    }

    /**
     * 获取本地服务器的地址（IP地址和端口号的组合）
     *
     * @param channel 服务器通道
     * @return 本地服务器的地址
     */
    public static String getLocalAddress(Channel channel) {
        String ip = getLocalIp(channel);
        int port = getLocalPort(channel);
        return ip + ADDRESS_SPLIT + port;
    }

    /**
     * 根据给定的IP地址和端口号生成地址字符串
     *
     * @param ip   IP地址
     * @param port 端口号
     * @return 地址字符串
     */
    public static String getAddress(String ip, int port) {
        return ip + ADDRESS_SPLIT + port;
    }

    /**
     * 获取本地服务器的WebSocket地址（带有WebSocket协议前缀）
     *
     * @param channel 服务器通道
     * @return 本地服务器的WebSocket地址
     */
    public static String getWsServerAddress(Channel channel) {
        String ip = getLocalIp(channel);
        int port = getLocalPort(channel);
        return WS_PROTOCOL + ip + ADDRESS_SPLIT + port;
    }

    /**
     * 解析地址字符串中的IP地址
     *
     * @param address 地址字符串
     * @return IP地址
     */
    public static String parseIp(String address) {
        String[] split = address.split(ADDRESS_SPLIT);
        return split[0];
    }

    /**
     * 解析地址字符串中的端口号
     *
     * @param address 地址字符串
     * @return 端口号
     */
    public static int parsePort(String address) {
        String[] split = address.split(ADDRESS_SPLIT);
        return Integer.parseInt(split[1]);
    }

    /**
     * 获取远程客户端的地址（IP地址和端口号的组合）
     *
     * @param channel 客户端通道
     * @return 远程客户端的地址
     */
    public static String getRemoteAddress(Channel channel) {
        if (channel != null) {
            String ip = getRemoteIp(channel);
            int port = getRemotePort(channel);
            return getAddress(ip, port);
        }
        return null;
    }
}

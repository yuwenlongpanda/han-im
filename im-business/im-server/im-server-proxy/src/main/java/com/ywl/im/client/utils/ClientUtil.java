package com.ywl.im.client.utils;

import com.ywl.im.client.core.impl.WsClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端管理器工具类
 */
@Slf4j
public class ClientUtil {

    /**
     * IP和端口号分割符
     */
    public static final String ADDRESS_SPLIT = ":";

    private static final Map<String, WsClient> clientMap = new ConcurrentHashMap<>();

    /**
     * 获取客户端映射表
     *
     * @return 客户端映射表
     */
    public static Map<String, WsClient> getClientMap() {
        return clientMap;
    }

    /**
     * 添加服务器作为其他服务器的客户端
     *
     * @param ip   IP地址
     * @param port 端口号
     * @param client WsClient对象
     */
    public static void addClient(String ip, int port, WsClient client) {
        getClientMap().put(createClientKey(ip, port), client);
    }

    /**
     * 获取服务器作为其他服务器的客户端
     *
     * @param address 服务器地址（IP:Port）
     * @return WsClient对象
     */
    public static WsClient getClient(String address) {
        return getClientMap().get(address);
    }

    /**
     * 当其他Netty服务下线时，移除当前服务器作为其他服务的客户端
     *
     * @param ip   IP地址
     * @param port 端口号
     */
    public static void removeClient(String ip, int port) {
        if (ip == null) {
            return;
        }
        String clientKey = createClientKey(ip, port);
        WsClient remove = getClientMap().remove(clientKey);
        if (remove != null) {
            remove.close();
        }
    }

    /**
     * 判断是否包含对应服务器的客户端
     *
     * @param ip   IP地址
     * @param port 端口号
     * @return true表示包含，false表示不包含
     */
    public static boolean containsClient(String ip, int port) {
        return getClientMap().containsKey(createClientKey(ip, port));
    }

    /**
     * 判断是否包含对应服务器的客户端
     *
     * @param address 服务器地址（IP:Port）
     * @return true表示包含，false表示不包含
     */
    public static boolean containsClient(String address) {
        return getClientMap().containsKey(address);
    }

    /**
     * 创建客户端Key
     *
     * @param ip   IP地址
     * @param port 端口号
     * @return 客户端Key
     */
    private static String createClientKey(String ip, int port) {
        return ip + ADDRESS_SPLIT + port;
    }
}

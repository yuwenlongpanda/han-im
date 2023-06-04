package com.ywl.im.client.core;

import com.ywl.framework.common.callback.ImCallback;

/**
 * 客户端接口
 */
public interface Client {

    /**
     * 获取服务器端口
     *
     * @return 服务器端口
     */
    int getServerPort();

    /**
     * 获取服务器IP
     *
     * @return 服务器IP
     */
    String getServerIp();

    /**
     * 打开连接
     *
     * @param callback 连接成功回调函数
     * @return 连接是否成功
     */
    boolean open(ImCallback callback);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 发送消息
     *
     * @param message 消息内容
     */
    void sendMessage(String message);
}

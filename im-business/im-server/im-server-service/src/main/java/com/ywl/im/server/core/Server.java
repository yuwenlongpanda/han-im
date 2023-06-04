package com.ywl.im.server.core;

import com.ywl.framework.common.callback.ImCallback;

public interface Server {
    /**
     * 获取服务器的端口号
     *
     * @return 服务器端口号
     */
    int getPort();

    /**
     * 获取服务器的IP地址
     *
     * @return 服务器IP地址
     */
    String getIp();

    /**
     * 启动服务器服务
     */
    void start(ImCallback callback);

    /**
     * 关闭服务器
     */
    void shutdown();

    /**
     * 检查服务器是否已启动
     *
     * @return 如果服务器已启动，则返回true；否则返回false
     */
    boolean isStart();

}

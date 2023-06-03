package com.ywl.im.common.config;

public interface IServer {
    /**
     * 获取server的端口号
     *
     * @return
     */
    int getPort();

    /**
     * 获取server的ip
     *
     * @return
     */
    String getIp();

    /**
     * 启动server服务
     */
    void start();

    /**
     * 关闭
     */
    void shutdown();

    /**
     * 是否启动
     *
     * @return
     */
    boolean isStart();

}

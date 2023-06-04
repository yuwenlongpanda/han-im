package com.ywl.im.server.core;

/**
 * 接口 ImProperties 定义了获取即时通讯服务器的配置属性的方法。
 */
public interface ImProperties {
    /**
     * 获取服务器的端口号
     *
     * @return 服务器端口号
     */
    int getPort();

    /**
     * 获取主线程池的线程数
     *
     * @return 主线程池的线程数
     */
    int getBossNthreads();

    /**
     * 获取工作线程池的线程数
     *
     * @return 工作线程池的线程数
     */
    int getWorkerNthreads();

    /**
     * 获取客户端读取空闲时间（秒）
     *
     * @return 客户端读取空闲时间
     */
    int getClientReaderIdleTimeSeconds();

    /**
     * 获取最大内容长度
     *
     * @return 最大内容长度
     */
    int getMaxContentLength();

    /**
     * 获取SO_BACKLOG参数的值
     *
     * @return SO_BACKLOG参数的值
     */
    int getSoBackLog();

    /**
     * 获取WebSocket的前缀
     *
     * @return WebSocket的前缀
     */
    String getWebSocketPrefix();
}


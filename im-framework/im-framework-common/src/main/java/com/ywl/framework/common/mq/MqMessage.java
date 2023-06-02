package com.ywl.framework.common.mq;


/**
 * MQ消息接口
 *
 * @param <T> 消息体类型
 * @author liao
 */
public interface MqMessage<T> {

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    default void product(T message) {
    }

    /**
     * 自定义发送消息
     *
     * @param mqConfig 消息配置
     * @param message  消息体
     */
    default void product(MqConfig mqConfig, T message) {
    }

    /**
     * 消费消息
     *
     * @param message 消息体
     */
    void consumer(T message);
}

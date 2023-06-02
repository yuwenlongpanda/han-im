package com.ywl.framework.common.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * MQ消息配置
 *
 * @author liao
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MqConfig implements Serializable {

    /**
     * tag
     */
    private String tag;
    /**
     * 设置了同一hashKey的消息可保证消费顺序
     */
    private String hashKey;
    /**
     * 延迟消息的延迟级别
     */
    private MqDelayLevel delayLevel;
}

package com.ywl.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImMqMessage implements Serializable {
    /**
     * 用于顺序重复消费
     */
    private String hashKey;
    /**
     * 用于防止重复消费
     */
    private String uuid;
    /**
     * 消息发送人
     */
    private String fromId;
    /**
     * 发送的内容
     */
    private String content;
    /**
     * 消息接收者id集合
     */
    private List<String> toIds;
}

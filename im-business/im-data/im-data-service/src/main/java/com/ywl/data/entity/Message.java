package com.ywl.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName message
 */
@Data
public class Message implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 消息发送人
     */
    private String fromId;

    /**
     * 消息在会话中的id 在当前会话中是连续的
     */
    private Integer messageIndex;

    /**
     * 会话id
     */
    private Integer sessionId;

    /**
     * 消息类型 1-文本 2-语音 3-图片 4-视频 5-撤回消息
     */
    private Integer type;

    /**
     * 发送的消息体
     */
    private String payload;

    /**
     * 消息创建的时间戳
     */
    private Long createTimeStamp;

    /**
     * 是否删除 1-是 0-否
     */
    private Integer isDeleted;

}
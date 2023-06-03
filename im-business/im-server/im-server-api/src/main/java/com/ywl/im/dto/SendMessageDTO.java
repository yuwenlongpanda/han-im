package com.ywl.im.dto;

import com.ywl.im.enums.MessageTypeEnum;
import com.ywl.im.enums.SessionTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 发送单人消息DTO
 *
 * @author zhou miao
 * @date 2022/04/22
 */
@Data
public class SendMessageDTO implements Serializable {
    private static final long serialVersionUID = 1;

    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 消息发送人id
     */
    private String fromId;
    private String fromAvatar;
    private String fromNickname;

    /**
     * 消息接收人id
     */
    private String toId;

    private String toAvatar;
    private String toNickname;

    /**
     * 消息json体 消息的具体内容 IM中台已经提供好的类型有文本 图片 音频 视频 文件，其他类型需要客户端自定义json
     *
     * 文本消息 type = com.msb.im.api.enums.MessageTypeEnum#TEXT 时 payload = {"text ":"文本消息 "}
     *
     * 语音消息 type = com.msb.im.api.enums.MessageTypeEnum#AUDIO 时 payload = {"name":"20200717120129175.m4a","contentType":"audio/m4a","url":"https://xxx/20200717120129175.m4a","duration":2.46,"size":15220}
     *
     * 图片消息 type = com.msb.im.api.enums.MessageTypeEnum#PIC 时 payload = {"name":"04531220.jpg","contentType":"image/jpeg","url":"https://xxx/04531220.jpg","width":1758,"height":765,"size":62988}
     *
     * 视频消息 type = com.msb.im.api.enums.MessageTypeEnum#VIDEO 时 payload = {"video":{"name":"1593738719905558_20200717114010716.mp4","contentType":"video/mp4","url":"视频连接","duration":46.766667,"width":544,"height":960,"size":7404683},"thumbnail":{"width":544,"height":960,"contentType":"image/jpg","url":"第一帧图片链接"}}
     *
     * 文件消息 type = com.msb.im.api.enums.MessageTypeEnum#FILE 时 payload = {"name":"04531220.txt","contentType":".txt","url":"https://xxx/04531220.txt","size":"字节数"}
     *
     * 文本消息 type = com.msb.im.api.enums.MessageTypeEnum#CUSTOM 时 payload = {"自定义json":"自定义json"}
     */
    private String payload;

    /**
     * 系统id
     */
    private Integer sysId;

    /**
     * 会话自定义信息
     *
     */
    private String sessionPayload;

    /**
     * 会话类型
     */
    private SessionTypeEnum sessionTypeEnum;
}

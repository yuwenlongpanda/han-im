package com.ywl.im.enums;

import com.msb.framework.common.model.IDict;

import java.io.Serializable;

/**
 * 消息类型
 *
 * @author zhou miao
 * @date 2022/04/18
 */
public enum MessageTypeEnum implements IDict<Integer>, Serializable {

    //
    TEXT(1, "文本消息"),
    AUDIO(2, "语音消息"),
    PIC(3, "图片消息"),
    VIDEO(4, "视频消息"),
    WITHDRAW(5, "撤回消息"),
    CUSTOM(6, "客户端自定义"),
    @Deprecated
    STORE_WAITER_TRANSFER_SESSION(7, "展示在会话中间的消息"),
    FILE(8, "文件消息"),
    ;

    private static final long serialVersionUID = 1L;
    private final Integer code;
    private final String text;

    MessageTypeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
        init(code, text);
    }

    public static boolean ofCode(int code) {
        IDict<Integer> dict = IDict.getByCode(MessageTypeEnum.class, code);
        return dict != null;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

}

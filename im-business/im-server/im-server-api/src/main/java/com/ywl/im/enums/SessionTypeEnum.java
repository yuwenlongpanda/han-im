package com.ywl.im.enums;


import com.ywl.framework.common.model.IDict;

/**
 * 会话类型
 *
 * @author zhou miao
 * @date 2022/04/18
 */
public enum SessionTypeEnum implements IDict<Integer> {
    //
    SINGLE(1, "单聊会话"),
    GROUP(2, "群聊会话"),
    STORE(3, "商铺客服会话"),
    CUSTOM(4, "自定义会话"),
    ;

    SessionTypeEnum(Integer code, String text) {
        init(code, text);
    }

}

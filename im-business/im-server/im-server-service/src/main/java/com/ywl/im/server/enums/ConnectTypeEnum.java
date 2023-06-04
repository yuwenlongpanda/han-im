package com.ywl.im.server.enums;


import com.ywl.framework.common.model.IDict;

/**
 * 客户端连接的通道类型
 */
public enum ConnectTypeEnum implements IDict<String> {
    //
    COMMON("1", "普通连接"),
    INTERNAL("2", "内部连接"),
    ;


    ConnectTypeEnum(String code, String text) {
        init(code, text);
    }

}

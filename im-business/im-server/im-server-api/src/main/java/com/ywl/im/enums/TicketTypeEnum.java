package com.ywl.im.enums;

import com.msb.framework.common.model.IDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhoumiao
 */
public enum TicketTypeEnum implements IDict<String> {
    //
    CONNECT_TICKET("connect", "连接的ticket"),
    CREATE_SINGLE_SESSION_TICKET("create_single_session", "创建单人会话ticket"),
    CREATE_GROUP_SESSION_TICKET("create_group_session", "创建群会话的ticket"),
    API_CURL_TICKET("api_curl", "调用api的ticket");
    String code;
    String text;

    TicketTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}

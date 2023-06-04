package com.ywl.im.server.models;

import com.ywl.im.enums.ConnectTypeEnum;
import lombok.Data;

/**
 * 握手请求参数
 *
 * @author zhou miao
 * @date 2022/04/09
 */
@Data
public class HandshakeParam {
    private String nickname;
    private String avatar;
    private String ticket;
    private String client;
    private String user;
    /**
     * {@link ConnectTypeEnum}
     */
    private String connect;
}
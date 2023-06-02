package com.ywl.framework.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class UserLoginInfo implements Serializable {
    /**
     * 用户id
     */
    private Long id;

    private Long employeeId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     *  系统
     */
    private Integer systemId;

    /**
     *  客户端
     */
    private Integer clientId;

    /**
     * unionid
     */
    private String unionId;

    private LocalDateTime tokenExpireTime;

    private String sessionId;

    private String token;
}

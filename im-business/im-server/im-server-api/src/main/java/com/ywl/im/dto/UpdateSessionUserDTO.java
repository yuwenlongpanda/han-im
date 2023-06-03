package com.ywl.im.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 修改会话用户信息DTO
 *
 * @author zhou miao
 * @date 2022/05/27
 */
@Data
@Accessors(chain = true)
public class UpdateSessionUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String userNickname;
    /**
     * 用户头像
     */
    private String userAvatar;
    private Integer systemId;
}

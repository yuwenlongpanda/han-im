package com.ywl.data.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName session
 */
@Data
public class Session implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 1-严选商城
     */
    private Integer sysId;

    /**
     * 1-单聊会话 2-群聊会话
     */
    private Integer type;

    /**
     * 群头像
     */
    private String groupAvatar;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 
     */
    private String createUser;

    /**
     * 
     */
    private String updateUser;

    /**
     * 是否删除 1-是 0-否
     */
    private Integer isDeleted;

    /**
     * 更新时间戳
     */
    private Long updateTimeStamp;

    /**
     * 会话自带信息
     */
    private String payload;

    /**
     * 
     */
    private Long createTimeStamp;

}
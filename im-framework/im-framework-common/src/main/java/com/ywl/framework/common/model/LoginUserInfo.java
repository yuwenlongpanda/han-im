package com.ywl.framework.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * //todo 优化
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
public class LoginUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long uid;
    /**
     * 用户唯一编号
     */
    private String userNo;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private Long deptId;
    private List<String> roles;
    /**
     * 是否是超管
     */
    private Integer managerFlag;
    /**
     * 登录来源: 1- 用户端；2- 后台管理 ，默认用户端
     */
    private String loginFrom;
    /**
     * 登录ip
     */
    private String loginIp;

}

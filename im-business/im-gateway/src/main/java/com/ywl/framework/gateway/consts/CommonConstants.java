package com.ywl.framework.gateway.consts;

/**
 * 常量
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年06月18日
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface CommonConstants {
    String LOGIN_USERNAME_KEY = "italk-";

    String ID_BZTAG_USER = "user";
    String ID_BZTAG_AVATAR = "avatar";
    String ID_BZTAG_GROUP = "group";
    String ID_BZTAG_ENTRANCE = "entrance";

    /**
     * 新用户 用户名前缀
     */
    String NEW_USER_PREFIX = "MCA-";
    /**
     * 新用户名称 转换的进制
     */
    int CUSTOM_NAME_RADIX = 16;

    /**
     * 登录类型,PC验证码,pc微信登录
     */
    Integer LOGINTYPE_PC = 1;
    /**
     * 登录类型,app验证码登录,app微信登录,app绑定微信登录绑定手机号
     */
    Integer LOGINTYPE_APP = 2;
    /**
     * 登录类型,微信h5登录
     */
    Integer LOGINTYPE_H5 = 3;
    /**
     * 登录类型,微信小程序登录
     */
    Integer LOGINTYPE_XCX = 4;
    /**
     * 登录类型,后台管理登录
     */
    Integer LOGINTYPE_HTGL = 5;
    /**
     * 登录类型,公众号登录
     */
    Integer LOGINTYPE_OFFACCOUNT = 6;
    /**
     * 登录类型,分销登录
     */
    Integer LOGINTYPE_SHARE = 7;
    /**
     * /**
     * 登录类型，靶场登录
     */
    Integer LOGINTYPE_WEBTARGET = 8;

}

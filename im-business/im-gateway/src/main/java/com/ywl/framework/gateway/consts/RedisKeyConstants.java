package com.ywl.framework.gateway.consts;

/**
 * @author zhoumiao
 * @date 2021-07-14
 */
public interface RedisKeyConstants {
    String WHITE_URL_KEY = "ywl:EDU:ADMIN:WHITE-URL:";

    /**
     * PC验证码,pc微信登录TOKEN
     */
    String LOGIN_TOKEN_BZID_PC = "LOGIN:TOKEN:BZID:PC:";
    /**
     * app验证码登录,app微信登录,app绑定微信登录绑定手机号登录TOKEN
     */
    String LOGIN_TOKEN_BZID_APP = "LOGIN:TOKEN:BZID:APP:";
    /**
     * 微信h5登录TOKEN
     */
    String LOGIN_TOKEN_BZID_H5 = "LOGIN:TOKEN:BZID:H5:";
    /**
     * 微信小程序登录TOKEN
     */
    String LOGIN_TOKEN_BZID_XCX = "LOGIN:TOKEN:BZID:XCX:";
    /**
     * 后台管理登录TOKEN
     */
    String LOGIN_TOKEN_BZID_HTGL = "LOGIN:TOKEN:BZID:HTGL:";
    /**
     * 微信公众号登录TOKEN
     */
    String LOGIN_TOKEN_BZID_OFFACCOUNT = "LOGIN:TOKEN:BZID:OFFACCOUNT:";
    /**
     * 微信公众号登录TOKEN
     */
    String LOGIN_TOKEN_BZID_SHARE = "LOGIN:TOKEN:BZID:SHARE:";
    /**
     * 靶场登录TOKEN
     */
    String LOGIN_TOKEN_BZID_WEBTARGET = "LOGIN:TOKEN:BZID:WEBTARGET:";
    /**
     * token过期时间,分钟
     */
    Integer TOKEN_EXPIRATION = 4320;
    /**
     * 优惠卷领取
     */
    String COUPON_RECEIVE = "ywl:EDU:COUPON:RECEIVE:";

    /**
     * 后台管理登录名
     */
    String LOGIN_LOCK_COUNT = "LOGIN:LOCK:COUNT";
    /**
     * 砍价并发控制
     */
    String CUT_CURRENT_CTL_LOCK = "LOCK:CUT:CTL";

    /**
     * 买一赠N活动领取人数控制锁
     */
    String COURSE_SHARE_RECEIVE_LOCK = "LOCK:COURSE:SHARE:NUM:RECEIVE";

    /**
     * 赠一得一领取人数控制锁
     */
    String COURSE_SHARE_GIVE_RECEIVE_LOCK = "LOCK:COURSE:SHARE:NUM:SHAREGIVE";

}
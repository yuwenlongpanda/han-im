package com.ywl.framework.redis.consts;

/**
 * 常用时间段（对应秒数）
 *
 * @author luozhan
 * @date 2022-7-9
 */
public interface Time {
    int SECOND = 1;

    int MINUTE = 60;

    int HOUR = 60 * MINUTE;

    int DAY = 24 * HOUR;

    int MONTH = 30 * DAY;

    int YEAR = 12 * MONTH;
    /**
     * forever = 50 years
     */
    int FOREVER = 50 * YEAR;


}
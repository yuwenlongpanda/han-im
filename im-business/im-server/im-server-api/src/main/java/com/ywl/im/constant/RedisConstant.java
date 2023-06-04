package com.ywl.im.constant;

public interface RedisConstant {
    // ===========================================统计相关的key==========================================================
    String SYS_ONLINE_USER_COUNT_KEY = "im:online:user:count:{sysId}";


    // ===========================================netty服务相关的key==========================================================
    /**
     * netty服务在redis中的记录
     * 数据结构：zset，{会话id：会话顺序}
     */
    String NETTY_SERVER_ZSET_KEY = "im:netty:server";
    int NETTY_SERVER_ZSET_EXPIRE_DAYS = 1;

    // ===========================================会话相关的key==========================================================
    /**
     * 用户的会话列表
     * 数据结构：zset，{会话id：会话顺序}
     */
    String USER_SESSION_ID_ZSET = "im:session:user:sessions:{sysId}:{userId}";
    String ADD_USER_SESSION_ID_ZSET_LOCK = "im:session:user:sessions:lock:{sysId}:{userId}";
    int USER_SESSION_ZSET_EXPIRED_DAYS = 90;

    /**
     * 会话内容
     * 数据结构：string 存储Session对象
     */
    String SESSION = "im:session:{sessionId}";
    int SESSION_EXPIRED_DAYS = 60;

    /**
     * 会话和用户的关联内容
     * 数据结构：string 存储sessionUser对象
     */
    String SESSION_USER = "im:session:user:{sessionId}:{userId}";
    int SESSION_USER_EXPIRED_DAYS = 60;

    /**
     * 用户会话未读数
     * 数据结构 string key：当前字符串+用户id+会话id value:未读数
     */
    String USER_SESSION_UNREAD = "im:session:user:unread:{userId}:{sessionId}";
    String ADD_USER_SESSION_UNREAD_LOCK = "im:session:user:unread:lock:{userId}:{sessionId}";
    int USER_SESSION_UNREAD_EXPIRE_DAYS = 20;

    /**
     * 用户会话未读数
     * 数据结构 string key：当前字符串+用户id+会话id value:未读数
     */
    String USER_SESSION_UNREAD_MAP = "im:session:user:unread:map:{userId}:{sessionId}";
    int USER_SESSION_UNREAD_MAP_EXPIRE_DAYS = 20;

    /**
     * 会话关联的用户id
     *
     * 数据结构：set 存储用户id
     */
    String SESSION_USER_ID_SET = "im:session:users:{sessionId}";
    String ADD_SESSION_USER_ID_SET_LOCK = "im:session:users:lock:{sessionId}";
    int SESSION_USERS_ID_EXPIRED_DAYS = 60;

    // ===========================================消息相关的key==========================================================
    /**
     * 会话中消息id
     * 数据结构：list 存储用户消息id
     */
    String SESSION_MESSAGE_ID_LIST = "im:message:session:messages:{sessionId}";
    String ADD_SESSION_MESSAGE_ID_LIST_LOCK = "im:message:session:messages:lock:{sessionId}";
    /**
     * 会话消息id列表过期时间
     */
    int SESSION_MESSAGE_ID_LIST_EXPIRED_DAYS = 90;

    /**
     * 消息
     * 数据结构：sting 存储消息
     */
    String MESSAGE = "im:message:{messageId}";
    /**
     * 消息过期时间
     */
    int MESSAGE_EXPIRED_DAYS = 60;

    /**
     * 会话最后一条消息
     * 数据结构：sting 存储会话最后一条消息
     */
    String SESSION_LAST_MESSAGE_ID = "im:message:last:{sessionId}";
    /**
     * 最后一条消息id过期时间
     */
    int SESSION_LAST_MESSAGE_ID_EXPIRED_DAYS = 60;

    //=================================================用户连接服务器地址相关============================================
    /**
     * 用户连接地址在redis中的key
     * {userId} : {ip:port}
     */
    String USER_WEBSOCKET_ADDRESS_KEY = "im:user:address:{sysId}:{userId}-{address}";

    /**
     * 用户连接的所有服务器
     */
    String USER_WEBSOCKET_ADDRESS_SET_KEY = "im:user:address:{sysId}:{userId}";
    // 用户连接的所有服务器的过期时间
    int USER_WEBSOCKET_ADDRESS_SET_TIME_DAYS = 1;

    //=================================================消息去重相关============================================
    /**
     * 客户端请求的id记录key
     * string结构进行记录 key是当前字符串+traceId value是空
     */
    String TRACE_ID_KEY = "im:trace:{traceId}";
    int TRACE_ID_EXPIRE_DAYS = 1;

    //=================================================id相关============================================
    /**
     * 消息id的key
     * string结构进行记录 key是当前字符串 value是id生成到多少了
     */
    String MESSAGE_ID_KEY = "im:id:message";
    // 生成消息id时的锁
    String ADD_MESSAGE_ID_LOCK = "im:id:message:lock";

    /**
     * 消息id的key
     * string结构进行记录 key是当前字符串 value是id生成到多少了
     */
    String SESSION_ID_KEY = "im:id:session";
    // 生成会话id时的锁
    String ADD_SESSION_ID_LOCK = "im:id:session:lock";

    /**
     * 会话和用户关联id的key
     * string结构进行记录 key是当前字符串 value是id生成到多少了
     */
    String SESSION_USER_ID_KEY = "im:id:sessionUser";
    // 生成会话和用户关联id时的锁
    String ADD_SESSION_USER_ID_LOCK = "im:id:sessionUser:lock";

    /**
     * 会话中消息连续id的key
     * string结构进行记录 key是当前字符串+sessionId value是id生成到多少了
     */
    String MESSAGE_INDEX_KEY = "im:id:sessionMessage:{sessionId}";
    int MESSAGE_INDEX_EXPIRE_DAYS = 90;

    // 生成会话中消息连续id时的锁 需要加上sessionId 只会锁一个会话内生成消息id
    String MESSAGE_INDEX_LOCK = "im:id:sessionMessage:lock:{sessionId}";

    //=================================================第三方系统配置相关key============================================
    /**
     * 数据结构 string
     */
    String THIRD_SYSTEM_KEY = "im:third:system:{sysId}";
    String ADD_THIRD_SYSTEM_KEY_LOCK = "im:third:system:lock";
    int THIRD_SYSTEM_EXPIRE_DAYS = 30;

    //=================================================mq消费去重============================================
    String MQ_MESSAGE_CONSUMED = "im:message:consumed:";
    int MQ_MESSAGE_CONSUMED_EXPIRE_DAYS = 1;

    //=================================================客服使用============================================
    /**
     * 数据结构 string
     * 用户心跳使用
     */
    String STORE_SESSION_USER_HEART_KEY = "im:store:user:heart:{sysId}:{storeId}:{userId}";

    /**
     * 数据结构zset
     * 商店所有客服
     */
    String STORE_WAITER_ZSET_KEY = "im:store:waiters:{sysId}:{storeId}";

    /**
     * 数据结构string
     * 用于客服连接时的ticket对应用户信息
     */
    String STORE_WAITER_TICKET_WAITER_KEY = "im:store:waiter:ticket:userInfo:{ticket}";

    /**
     * 数据结构string
     * 用于客服连接时的ticket对应storeId
     */
    String STORE_WAITER_TICKET_STORE_KEY = "im:store:waiter:ticket:storeId:{storeId}";

    /**
     * 数据结构 string
     * 客服心跳使用
     */
    String STORE_SESSION_WAITER_HEART_KEY = "im:store:waiter:heart:{sysId}:{storeId}:{waiterId}";

    /**
     * 客服连接的所有服务器
     */
    String STORE_WAITER_WEBSOCKET_ADDRESS_SET_KEY = "im:store:waiter:addresss:{sysId}:{storeId}:{waiterId}";
    // 客服连接的所有服务器的过期时间
    int STORE_WAITER_WEBSOCKET_ADDRESS_SET_TIME_DAYS = 1;

    /**
     * 客服接待用户的队列
     */
    String STORE_RECEIVE_USER_LIST_KEY = "im:store:receive:user:{sysId}:{storeId}:{waiterId}";

    //=================================================锁相关============================================
    String IM_CONSUME_SEND_MESSAGE_LOCK = "im:lock:consumeSendMessage:{sysId}:{fromIdAndToIdMd5Sum}";
    String IM_SEND_MESSAGE_LOCK = "im:lock:sendMessage:{sysId}:{fromIdAndToIdMd5Sum}";


    /**
     * 替换占位符
     * 如：format("im:session:{id}", "1") //返回 im:session:1
     */
    static String format(String keyExpression, Object... args) {
        StringBuilder sb = new StringBuilder(keyExpression);
        int p, i = 0;
        while ((p = sb.indexOf("{")) >= 0 && i < args.length) {
            sb.replace(p, sb.indexOf("}") + 1, args[i++].toString());
        }

        return sb.toString();
    }
}

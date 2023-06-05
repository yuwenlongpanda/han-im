package com.ywl.im.client.handler;

import com.ywl.framework.redis.RedisClient;
import com.ywl.im.client.utils.NetworkAddressUtil;
import com.ywl.im.constant.RedisConstant;
import com.ywl.im.models.ServerAddress;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * im服务redis相关的处理
 *
 * @author zhoumiao
 * @date 2022-04-17
 */
@Service
public class RedisHandler {
    @Resource
    private RedisClient redisClient;

    /**
     * 释放服务器连接数
     *
     * @param address 服务器地址
     */
    public void decrementServerConnectedCount(String address) {
        redisClient.zIncrementScore(RedisConstant.NETTY_SERVER_ZSET_KEY, address, -1d);
    }


    /**
     * 新增服务器连接数
     *
     * @param address 服务器地址
     */
    public void incrementServerConnectedCount(String address) {
        redisClient.zIncrementScore(RedisConstant.NETTY_SERVER_ZSET_KEY, address, 1d);
    }
//
//    /**
//     * 删除服务器
//     *
//     * @param address 服务器
//     */
//    public void removeNettyServer(String address) {
//        redisClient.zDel(NETTY_SERVER_ZSET_KEY, address);
//    }

    /**
     * 删除用户连接的服务器地址记录
     *
     * @param userId  用户id
     * @param address 连接服务器地址
     * @param sysId   系统id
     */
    public void removeUserServerAddress(String userId, String address, int sysId) {
        String userAddressKey = getUserAddressKey(sysId, userId, address);
        redisClient.delete(userAddressKey);
    }


//    /**
//     * 记录用户连接的服务器地址 ip:port
//     *
//     * @param userId 用户id
//     */
//    public void addUserServerAddress(String userId, String address, int sysId) {
//        String userAddressKey = getUserAddressKey(sysId, userId, address);
//        redisClient.set(userAddressKey, "", imProperties.getClientRenewalSeconds());
//
//        addUserServerAddressSet(userId, address, sysId);
//    }
//
//    /**
//     * 记录用户连接的服务器地址 ip:port
//     *
//     * @param userId  用户id
//     * @param address 用户连接的服务器
//     * @param sysId   系统id
//     */
//    public void renewalUserServerAddress(String userId, String address, int sysId) {
//        String userAddressKey = getUserAddressKey(sysId, userId, address);
//        redisClient.set(userAddressKey, "", imProperties.getClientRenewalSeconds());
//
//        addUserServerAddressSet(userId, address, sysId);
//    }

    /**
     * 用户连接的服务器记录中是否存在指定服务器
     *
     * @param userId  用户id
     * @param address 用户连接地址
     * @param sysId   系统id
     * @return 用户连接的服务器记录中是否存在指定服务器
     */
    public boolean existUserAddress(String userId, String address, int sysId) {
        String userAddressKey = getUserAddressKey(sysId, userId, address);
        return redisClient.exists(userAddressKey);
    }


    /**
     * 新增用户连接的服务器记录
     *
     * @param userId
     * @param address
     */
    public void addUserServerAddressSet(String userId, String address, int sysId) {
        String userAddressSetKey = getUserAddressSetKey(sysId, userId);
        redisClient.sAdd(userAddressSetKey, address);
        redisClient.expire(userAddressSetKey, RedisConstant.USER_WEBSOCKET_ADDRESS_SET_TIME_DAYS, TimeUnit.DAYS);
    }

    /**
     * 查询用户连接的所有服务器
     *
     * @param userId 用户id
     */
    public Set<String> userServerAddressSet(String userId, int sysId) {
        String userAddressSetKey = getUserAddressSetKey(sysId, userId);
        return redisClient.sGet(userAddressSetKey);
    }
//
//    /**
//     * 移除用户连接的某台服务器
//     *
//     * @param userId
//     * @param address
//     * @param sysId
//     */
//    public void removeUserServerAddressSet(String userId, String address, int sysId) {
//        String userAddressSetKey = getUserAddressSetKey(sysId, userId);
//        redisClient.srm(userAddressSetKey, address);
//    }
//
//    /**
//     * 是否存在已经消费过的id
//     *
//     * @param traceId
//     * @return
//     */
//    public boolean existTraceId(String traceId) {
//        String traceIdKey = getTraceIdKey(traceId);
//        return redisClient.exists(traceIdKey);
//    }
//
//    /**
//     * 添加已经消费过的id
//     *
//     * @param traceId
//     */
//    public void addTraceId(String traceId) {
//        String traceIdKey = getTraceIdKey(traceId);
//        redisClient.set(traceIdKey, "", TRACE_ID_EXPIRE_DAYS, TimeUnit.DAYS);
//    }
//
//    /**
//     * 添加netty服务到redis中
//     *
//     * @param ip
//     * @param port
//     */
//    public void addNettyServer(String ip, int port) {
//        redisClient.zAdd(NETTY_SERVER_ZSET_KEY, AddressUtil.address(ip, port), 0L);
//    }
//

    /**
     * 添加netty服务到redis中
     *
     * @param ip
     * @param port
     */
    public void addNettyServerIfNotExist(String ip, int port) {
        redisClient.zAddIfNotExist(RedisConstant.NETTY_SERVER_ZSET_KEY, NetworkAddressUtil.getAddress(ip, port), 0L);
    }
//
//    /**
//     * 是否集群内ip
//     *
//     * @param ip
//     * @return
//     */
//    public boolean containsNettyServer(String ip) {
//        LinkedHashSet<String> serverAddresss = redisClient.zAll(NETTY_SERVER_ZSET_KEY);
//        for (String addresss : serverAddresss) {
//            if (addresss.startsWith(ip)) {
//                return true;
//            }
//        }
//        return false;
//    }
//

    /**
     * 获取 Redis 中记录的所有 Netty 服务地址
     *
     * @return 所有 Netty 服务地址的列表
     */
    public List<ServerAddress> getAllNettyServers() {
        // 从 Redis 中获取所有 Netty 服务地址
        LinkedHashSet<String> serverAddresses = redisClient.zAll(RedisConstant.NETTY_SERVER_ZSET_KEY);
        if (serverAddresses != null) {
            List<ServerAddress> servers = new ArrayList<>(serverAddresses.size());
            for (String address : serverAddresses) {
                // 解析地址中的 IP 和端口
                String ip = NetworkAddressUtil.parseIp(address);
                int port = NetworkAddressUtil.parsePort(address);
                // 创建 ServerAddress 对象，并设置 IP 和端口
                ServerAddress server = new ServerAddress();
                server.setIp(ip);
                server.setPort(port);
                // 将 ServerAddress 对象添加到列表中
                servers.add(server);
            }
            return servers;
        }
        // 如果 Redis 中没有记录，则返回空列表
        return Collections.emptyList();
    }

//
//    /**
//     * 用户端续期商铺会话心跳
//     */
//    public void renewalUserStoreSessionHeart(Integer sysId, Long storeId, String userId) {
//        String userStoreSessionHeartKey = getUserStoreSessionHeartKey(sysId, storeId, userId);
//        redisClient.set(userStoreSessionHeartKey, "", storeProperties.getStoreUserHeartExpireSeconds(), TimeUnit.SECONDS);
//    }
//
//    public boolean existUserStoreSessionHeart(Integer sysId, Long storeId, String userId) {
//        String userStoreSessionHeartKey = getUserStoreSessionHeartKey(sysId, storeId, userId);
//        return redisClient.exists(userStoreSessionHeartKey);
//    }
//
//
//    // ==================================================================客服============================================
//
//    /**
//     * 记录客服连接的服务器地址 ip:port
//     */
//    public void addStoreWaiter(String userId, String address, int sysId, Long storeId) {
//        String storeWaiterHeartKey = getStoreWaiterHeartKey(sysId, storeId, userId);
//        redisClient.set(storeWaiterHeartKey, "", imProperties.getClientRenewalSeconds());
//
//        addStoreWaiterServerAddressSet(userId, address, sysId, storeId);
//    }
//
//
//    /**
//     * 新增客服连接的服务器记录
//     *
//     * @param userId
//     * @param address
//     */
//    public void addStoreWaiterServerAddressSet(String userId, String address, int sysId, Long storeId) {
//        String userAddressSetKey = getStoreWaiterAddressSetKey(sysId, storeId, userId);
//        redisClient.sAdd(userAddressSetKey, address);
//        redisClient.expire(userAddressSetKey, STORE_WAITER_WEBSOCKET_ADDRESS_SET_TIME_DAYS, TimeUnit.DAYS);
//    }
//
//    public Set<String> storeWaiterServerAddressSet(String userId, int sysId, Long storeId) {
//        String storeWaiterAddressSetKey = getStoreWaiterAddressSetKey(sysId, storeId, userId);
//        return redisClient.sGet(storeWaiterAddressSetKey);
//    }
//
//    /**
//     * 是否存在客服心跳
//     *
//     * @param userId
//     * @param sysId
//     * @param storeId
//     * @return
//     */
//    public boolean existStoreWaiterHeart(String userId, int sysId, Long storeId) {
//        String waiterAddressKey = getStoreWaiterHeartKey(sysId, storeId, userId);
//        return redisClient.exists(waiterAddressKey);
//    }
//
//    /**
//     * 客服端续期商铺会话心跳
//     */
//    public void renewalStoreWaiterHeart(Integer sysId, Long storeId, String waiterId, String localAddress) {
//        String storeWaiterHeartKey = getStoreWaiterHeartKey(sysId, storeId, waiterId);
//        redisClient.set(storeWaiterHeartKey, "", storeProperties.getStoreWaiterHeartExpireSeconds(), TimeUnit.SECONDS);
//        addStoreWaiterServerAddressSet(waiterId, localAddress, sysId, storeId);
//
//    }
//
//    /**
//     * 删除客服商铺会话心跳
//     */
//    public void deleteStoreWaiterHeart(Integer sysId, Long storeId, String waiterId) {
//        String storeWaiterHeartKey = getStoreWaiterHeartKey(sysId, storeId, waiterId);
//        redisClient.delete(storeWaiterHeartKey, "");
//    }
//
//    /**
//     * 用户端分配客服
//     */
//    public String allocateStoreWaiter(Integer sysId, Long storeId) {
//        String storeWaiterKey = getStoreWaiterZsetKey(sysId, storeId);
//        if (!redisClient.exists(storeWaiterKey)) {
//            return null;
//        }
//        Object o = redisClient.zGet(storeWaiterKey, 0);
//        if (o == null) {
//            return null;
//        }
//        String waiterId = o.toString();
//        if (!existStoreWaiterHeart(waiterId, sysId, storeId)) {
//            deleteStoreWaiter(sysId, storeId, waiterId);
//            return allocateStoreWaiter(sysId, storeId);
//        }
//        return waiterId;
//    }
//
//    public Long deleteStoreWaiter(Integer sysId, Long storeId, String waiterId) {
//        String storeWaiterKey = getStoreWaiterZsetKey(sysId, storeId);
//        return redisClient.zRemove(storeWaiterKey, waiterId);
//    }
//
//    public void addStoreWaiter(Integer sysId, Long storeId, String waiterId) {
//        String storeWaiterKey = getStoreWaiterZsetKey(sysId, storeId);
//        redisClient.zAdd(storeWaiterKey, waiterId, 0L);
//    }
//
//    public void setStoreWaiterSessionNum(Integer sysId, Long storeId, String waiterId, Long sessionNum) {
//        String storeWaiterKey = getStoreWaiterZsetKey(sysId, storeId);
//        redisClient.zAdd(storeWaiterKey, waiterId, sessionNum);
//    }
//
//    public boolean existStoreWaiter(Integer sysId, Long storeId, String waiterId) {
//        String storeWaiterKey = getStoreWaiterZsetKey(sysId, storeId);
//        return redisClient.zExist(storeWaiterKey, waiterId);
//    }
//
//    public long incrementStoreWaiter(Integer sysId, Long storeId, String waiterId) {
//        String storeWaiterKey = getStoreWaiterZsetKey(sysId, storeId);
//        return redisClient.zIncrementScore(storeWaiterKey, waiterId, 1d).longValue();
//    }
//
//
//    public void setWaiterConnectTicket(Long storeId, String ticket, UserLoginInfo userLoginInfo, long tokenExpireMinutes) {
//        String storeWaiterTokenKey = getStoreWaiterTicketKey(ticket);
//        String storeWaiterTicketStoreKey = getStoreWaiterTicketStoreKey(ticket);
//        redisClient.set(storeWaiterTokenKey, userLoginInfo, tokenExpireMinutes, TimeUnit.MINUTES);
//        redisClient.set(storeWaiterTicketStoreKey, storeId, tokenExpireMinutes, TimeUnit.MINUTES);
//    }
//
//    public UserLoginInfo getWaiterTicketUser(String ticket) {
//        String storeWaiterTokenKey = getStoreWaiterTicketKey(ticket);
//        return redisClient.get(storeWaiterTokenKey);
//    }
//
//    public void removeWaiterTicketUser(String ticket) {
//        String storeWaiterTokenKey = getStoreWaiterTicketKey(ticket);
//        String storeWaiterTicketStoreKey = getStoreWaiterTicketStoreKey(ticket);
//        redisClient.delete(storeWaiterTokenKey, storeWaiterTicketStoreKey);
//    }
//
//    public String getWaiterTicketStoreId(String ticket) {
//        String storeWaiterTicketStoreKey = getStoreWaiterTicketStoreKey(ticket);
//        Object storeId = redisClient.get(storeWaiterTicketStoreKey);
//        if (storeId == null) {
//            return null;
//        } else {
//            return storeId.toString();
//        }
//    }
//
//
//    // ==============================================key==========================================
//    private String getStoreWaiterTicketKey(String ticket) {
//        return RedisConstant.format(STORE_WAITER_TICKET_WAITER_KEY, ticket);
//    }
//
//    private String getStoreWaiterTicketStoreKey(String ticket) {
//        return RedisConstant.format(STORE_WAITER_TICKET_STORE_KEY, ticket);
//    }
//
//    private String getStoreWaiterHeartKey(Integer sysId, Long storeId, String waiterId) {
//        return RedisConstant.format(STORE_SESSION_WAITER_HEART_KEY, sysId, storeId, waiterId);
//    }
//
//    private String getStoreWaiterZsetKey(Integer sysId, Long storeId) {
//        return RedisConstant.format(STORE_WAITER_ZSET_KEY, sysId, storeId);
//    }
//
//    private String getUserStoreSessionHeartKey(Integer sysId, Long storeId, String userId) {
//        return RedisConstant.format(STORE_SESSION_USER_HEART_KEY, sysId, storeId, userId);
//    }

    private String getUserAddressKey(int sysId, String userId, String address) {
        return RedisConstant.format(RedisConstant.USER_WEBSOCKET_ADDRESS_KEY, sysId, userId, address);
    }

    private String getUserAddressSetKey(int sysId, String userId) {
        return RedisConstant.format(RedisConstant.USER_WEBSOCKET_ADDRESS_SET_KEY, sysId, userId);
    }
//
//    /*private String getStoreWaiterAddressKey(int sysId, String userId, String address, long storeId) {
//        return RedisConstant.format(STORE_WAITER_WEBSOCKET_ADDRESS_KEY, sysId, storeId, userId, address);
//    }*/
//
//    private String getStoreWaiterAddressSetKey(int sysId, Long storeId, String userId) {
//        return RedisConstant.format(STORE_WAITER_WEBSOCKET_ADDRESS_SET_KEY, sysId, storeId, userId);
//    }
//
//    private String getTraceIdKey(String traceId) {
//        return RedisConstant.format(TRACE_ID_KEY, traceId);
//
//    }
}

package com.ywl.im.server.handler;

import com.ywl.im.server.utils.NetworkAddressUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * UserChannelHandler 是一个用户和管理通道之间关联关系的工具类。
 * 它记录了用户和管理通道之间的对应关系，以及用户与其会话通道的关系。
 * 通过该类，可以轻松地获取用户的通道集合，根据通道获取用户ID和系统ID，并进行消息的推送和转发操作。
 * 该类还提供了添加和移除通道的方法，以及管理用户连接地址和服务器连接数的功能。
 * 注意：该类是线程安全的，适用于多线程环境下的用户通信管理。
 */

@Slf4j
@Component
public class UserChannelHandler {
    private static final String SYSTEM_USER_SPLIT = "-";

    /**
     * 记录通道和用户的对应关系
     * {channelId -> userId-sysId}
     */
    private static final ConcurrentMap<String, String> CHANNEL_USER_MAP = new ConcurrentHashMap<>();

    /**
     * 记录用户对应session和channel的关系
     * {userId-sysId -> {[channel1,channel2]}}
     */
    private static final ConcurrentMap<String, Set<Channel>> USER_SESSION_CHANNELS_MAP = new ConcurrentHashMap<>();

    @Resource
    private RedisHandler redisHandler;
//
//    @Resource
//    private NettyClusterManager nettyClusterManager;

    /**
     * 获取用户的通道集合
     *
     * @param systemId 系统ID
     * @param userId   用户ID
     * @return 用户通道集合
     */
    public Set<Channel> getUserChannels(Integer systemId, String userId) {
        if (userId == null || systemId == null) {
            return null;
        } else {
            String systemAndUserKey = getSystemAndUserKey(systemId, userId);
            return USER_SESSION_CHANNELS_MAP.getOrDefault(systemAndUserKey, new HashSet<>(1));
        }
    }

    /**
     * 获取系统和用户的对应通道集合的RedisConstant组合键
     *
     * @param systemId 系统ID
     * @param userId   用户ID
     * @return 系统和用户的组合键
     */
    private String getSystemAndUserKey(Integer systemId, String userId) {
        return systemId + SYSTEM_USER_SPLIT + userId;
    }

    /**
     * 解析用户ID
     *
     * @param userIdAndSystemId 用户ID和系统ID的组合字符串
     * @return 用户ID
     */
    private String parseUserId(String userIdAndSystemId) {
        String[] split = userIdAndSystemId.split(SYSTEM_USER_SPLIT);
        return split[1];
    }

    /**
     * 解析系统ID
     *
     * @param userIdAndSystemId 用户ID和系统ID的组合字符串
     * @return 系统ID
     */
    private Integer parseSystemId(String userIdAndSystemId) {
        String[] split = userIdAndSystemId.split(SYSTEM_USER_SPLIT);
        return Integer.parseInt(split[0]);
    }

    /**
     * 通过通道获取用户ID
     *
     * @param userChannel 用户通道
     * @return 用户ID
     */
    public String getUserId(Channel userChannel) {
        String channelId = userChannel.id().asLongText();
        String userIdAndSystemId = CHANNEL_USER_MAP.get(channelId);
        if (userIdAndSystemId == null) {
            return null;
        }
        return parseUserId(userIdAndSystemId);
    }

    /**
     * 通过通道获取系统ID
     *
     * @param userChannel 用户通道
     * @return 系统ID
     */
    public Integer getSystemId(Channel userChannel) {
        String channelId = userChannel.id().asLongText();
        String userIdAndSystemId = CHANNEL_USER_MAP.get(channelId);
        if (userIdAndSystemId == null) {
            return null;
        }
        return parseSystemId(userIdAndSystemId);
    }

//    /**
//     * 校验用户的心跳以及所在服务器，然后推送或者转发消息
//     *
//     * @param sysId    系统ID
//     * @param toUserId 接收人ID
//     * @param model    消息
//     */
//    public void pushOrForwardMessage(Integer sysId, String toUserId, RspMessageProto.Model model) {
//        // todo 待优化 多线程实现
//        Set<String> listUserServerAddress = redisService.userServerAddressSet(toUserId, sysId);
//        for (String address : listUserServerAddress) {
//            // 用户已断线
//            if (!redisService.existUserAddress(toUserId, address, sysId)) {
//                log.info("服务器不存在 删除用户记录的服务器 {} {}", toUserId, address);
//                redisService.removeUserServerAddressSet(toUserId, address, sysId);
//                continue;
//            }
//            if (ImServerContext.isCurrentServer(address)) {
//                pushMessage(sysId, toUserId, model);
//            } else {
//                ServerAsClientAble server = (ServerAsClientAble) ImServerContext.getServer();
//                IClient asClient = server.getAsClient(address);
//                // 服务器失联 删除服务器信息
//                if (asClient == null) {
//                    log.error("服务器 {} 在当前服务器中不存在客户端", server);
//                } else {
//                    asClient.sendMessage(model);
//                }
//            }
//        }
//    }
//
//    /**
//     * 推送消息给某个人
//     *
//     * @param systemId 系统ID
//     * @param toId     接收人ID
//     * @param model    消息
//     */
//    public void pushMessage(Integer systemId, String toId, RspMessageProto.Model model) {
//        if (model == null || toId == null || systemId == null) {
//            return;
//        }
//        log.info("通用通道 服务器推送消息 {} {} {}", model, toId, systemId);
//        String userSystemKey = getSystemAndUserKey(systemId, toId);
//        Set<Channel> channels = USER_SESSION_CHANNELS_MAP.getOrDefault(userSystemKey, Collections.emptySet());
//        for (Channel channel : channels) {
//            if (channel.isActive()) {
//                channel.writeAndFlush(RspFrameUtil.createRspFrame(model));
//            } else {
//                removeChannel(channel);
//            }
//        }
//    }

    /**
     * 记录连接的通道：记录通道和用户的关联信息，记录用户会话通道的关系，记录用户连接的服务器地址，记录服务器当前连接数
     *
     * @param channel  用户通道
     * @param systemId 系统ID
     * @param userId   用户ID
     * @return 是否添加成功
     */
    public boolean addChannel(Channel channel, Integer systemId, String userId) {
        addChannelUserRelation(systemId, userId, channel);
        addUserSessionChannel(systemId, userId, channel);

        String serverAddress = NetworkAddressUtil.getLocalAddress(channel);

        redisHandler.addUserServerAddress(userId, serverAddress, systemId);
        redisHandler.incrementServerConnectedCount(serverAddress);
//        nettyClusterManager.increment(systemId);
        return true;
    }

    /**
     * 删除内存中管理的通道、删除服务器连接数、用户连接通道为空时删除用户连接地址
     *
     * @param removeChannel 被移除的通道
     */
    public void removeChannel(Channel removeChannel) {
        String systemAndUserId = removeChannelUserRelation(removeChannel);

        if (systemAndUserId != null) {
            removeUserSessionChannel(removeChannel, systemAndUserId);
        }

        // 释放服务器连接数
        redisHandler.decrementServerConnectedCount(NetworkAddressUtil.getLocalAddress(removeChannel));

        // 用户在线统计
//        nettyClusterManager.decrement(parseSystemId(systemAndUserId));

        // 释放通道
        removeChannel.close();
    }

    private void removeUserSessionChannel(Channel removeChannel, String systemAndUserId) {
        Set<Channel> userChannels = USER_SESSION_CHANNELS_MAP.getOrDefault(systemAndUserId, Collections.emptySet());
        userChannels.remove(removeChannel);
        if (userChannels.isEmpty()) {
            USER_SESSION_CHANNELS_MAP.remove(systemAndUserId);
            redisHandler.removeUserServerAddress(parseUserId(systemAndUserId), NetworkAddressUtil.getLocalAddress(removeChannel), parseSystemId(systemAndUserId));
        }
    }

    /**
     * 添加用户会话通道
     *
     * @param systemId 系统ID
     * @param userId   用户ID
     * @param channel  通道
     */
    private void addUserSessionChannel(Integer systemId, String userId, Channel channel) {
        String systemAndUserKey = getSystemAndUserKey(systemId, userId);
        Set<Channel> userChannels = USER_SESSION_CHANNELS_MAP.getOrDefault(systemAndUserKey, new HashSet<>(1));
        userChannels.add(channel);
        USER_SESSION_CHANNELS_MAP.put(systemAndUserKey, userChannels);
    }

    /**
     * 添加通道和用户的关联
     *
     * @param systemId 系统ID
     * @param userId   用户ID
     * @param channel  通道
     */
    private void addChannelUserRelation(Integer systemId, String userId, Channel channel) {
        String userAndSystem = getSystemAndUserKey(systemId, userId);
        CHANNEL_USER_MAP.put(channel.id().asLongText(), userAndSystem);
    }

    /**
     * 删除通道和用户的关联
     *
     * @param removerChannel 被移除的通道
     * @return 用户系统ID
     */
    private String removeChannelUserRelation(Channel removerChannel) {
        return CHANNEL_USER_MAP.remove(removerChannel.id().asLongText());
    }
}
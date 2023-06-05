package com.ywl.im.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.ywl.framework.mq.annotation.ConsumerGroup;
import com.ywl.im.client.core.impl.WsClient;
import com.ywl.im.client.handler.RedisHandler;
import com.ywl.im.client.utils.ClientUtil;
import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.im.mq.topic.ImMessageTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@ConsumerGroup
@Component
@Slf4j
public class ImMessageConsumer implements ImMessageTopic {

    @Autowired
    private RedisHandler redisHandler;

    @Override
    public void consumer(ImMqMessage mqMessage) {
        try {
            log.info("消费mq {}", mqMessage);

            List<String> toIds = mqMessage.getToIds();
            for (String toId : toIds) {
                // 获取用户所在的服务器地址集合
                Set<String> serverAddressSet = redisHandler.userServerAddressSet(toId, 1);
                for (String address : serverAddressSet) {
                    // 检查用户是否已断线
                    if (!redisHandler.existUserAddress(toId, address, 1)) {
                        log.info("服务器不存在 删除用户记录的服务器 {} {}", toId, address);
                        // 移除用户记录的服务器
                        redisHandler.removeUserServerAddress(toId, address, 1);
                        continue;
                    }

                    // 获取与服务器地址对应的客户端
                    WsClient client = ClientUtil.getClient(address);
                    // 检查服务器是否失联
                    if (client == null) {
                        log.error("服务器 {} 在当前服务器中不存在客户端", address);
                    } else {
                        // 向客户端发送消息
                        client.sendMessage(JSONObject.toJSONString(mqMessage));
                    }
                }
            }
        } catch (Exception e) {
            log.error("消费错误 {}", e.getMessage());
            // todo 需要告警
        }
    }

}

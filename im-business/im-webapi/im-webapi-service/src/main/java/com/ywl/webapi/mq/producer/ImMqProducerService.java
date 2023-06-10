package com.ywl.webapi.mq.producer;

import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.framework.common.mq.MqConfig;
import com.ywl.webapi.mq.topic.ImMessageTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhou miao
 * @create: 2022/04/28
 */
@Service
@Slf4j
public class ImMqProducerService {
    @Resource
    private ImMessageTopic imMessageTopic;

    public void sendMq(ImMqMessage imMqMessage) {
        try {
            if (imMqMessage == null) {
                log.error("发送mq消息为空{}", imMqMessage);
                return;
            }
            String hashKey = imMqMessage.getHashKey();
            if (hashKey != null) {
                MqConfig mqConfig = new MqConfig();
                mqConfig.setHashKey(hashKey);
                mqConfig.setTag("tag");
                imMessageTopic.product(mqConfig, imMqMessage);
            } else {
                imMessageTopic.product(imMqMessage);
            }
        } catch (Exception e) {
            log.error("发送mq消息失败 {}", e.getMessage());
        }
    }

}

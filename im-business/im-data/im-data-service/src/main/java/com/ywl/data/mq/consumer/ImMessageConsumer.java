package com.ywl.data.mq.consumer;

import com.ywl.data.mq.topic.ImMessageTopic;
import com.ywl.data.service.MessageService;
import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.framework.mq.annotation.ConsumerGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ConsumerGroup
@Component
@Slf4j
public class ImMessageConsumer implements ImMessageTopic {

    @Autowired
    private MessageService messageService;

    @Override
    public void consumer(ImMqMessage mqMessage) {
        try {
            log.info("消费mq {}", mqMessage);

            messageService.save(mqMessage);
        } catch (Exception e) {
            log.error("消费错误 {}", e.getMessage());
            // todo 需要告警
        }
    }

}

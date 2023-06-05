package com.ywl.im.mq.topic;

import com.ywl.framework.common.mq.MqMessage;
import com.ywl.framework.common.mq.annotation.Topic;
import com.ywl.im.mq.model.ImMqMessage;

@Topic("IM-MESSAGE")
public interface ImMessageTopic extends MqMessage<ImMqMessage> {

}

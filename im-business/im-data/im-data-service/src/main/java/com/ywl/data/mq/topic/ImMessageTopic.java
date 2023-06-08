package com.ywl.data.mq.topic;

import com.ywl.framework.common.mq.MqMessage;
import com.ywl.framework.common.mq.annotation.Topic;
import com.ywl.framework.common.model.ImMqMessage;

@Topic("IM-MESSAGE")
public interface ImMessageTopic extends MqMessage<ImMqMessage> {

}

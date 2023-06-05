package com.ywl.product.mq.topic;

import com.ywl.framework.common.model.ImMqMessage;
import com.ywl.framework.common.mq.MqMessage;
import com.ywl.framework.common.mq.annotation.Topic;

/**
 * @author zhou miao
 * @date 2022/04/26
 */
@Topic("IM-MESSAGE")
public interface ImMessageTopic extends MqMessage<ImMqMessage> {

}

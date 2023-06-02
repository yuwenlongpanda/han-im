package com.ywl.framework.mq;

import com.ywl.framework.common.utils.SpringContextUtil;
import com.ywl.framework.common.mq.annotation.Topic;
import com.ywl.framework.common.mq.MqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.exception.RequestTimeoutException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.MimeTypeUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liao
 */
@Slf4j
public class MessageQueueInvocationHandler extends RocketMQTemplate implements InvocationHandler {

    /**
     * 缓存topic 对应的注解
     */
    private static final Map<Class<?>, Topic> MESSAGE_QUEUE_MAP = new ConcurrentHashMap<>();

    private static final SendCallback DEFAULT = new SendCallback() {
        @Override
        public void onSuccess(SendResult sendResult) {
            log.info("message onSuccess {}", sendResult);
        }

        @Override
        public void onException(Throwable e) {
            log.info("message onException", e);
        }
    };

    public static Object generateProxyClass(Class<?> type) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new MessageQueueInvocationHandler());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, RequestTimeoutException, MQBrokerException, RemotingException, InterruptedException, MQClientException {

        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        Topic messageQueue = getMessageQueue(proxy);

        String topic = SpringContextUtil.getSpringValue(messageQueue.value());

        MqConfig mqConfig = null;
        Object body;

        if (args.length > 1) {
            mqConfig = (MqConfig) args[0];
            body = args[1];
        } else if (args.length == 1) {
            body = args[0];
        } else {
            log.warn("mq 发送失败，代理方法有问题");
            return null;
        }
        RocketMQTemplate rocketTemplate = SpringContextUtil.getBean(RocketMQTemplate.class);
        DefaultMQProducer producer = rocketTemplate.getProducer();

        HashMap<String, Object> headersMap = new HashMap<>();
        MessageHeaders messageHeaders = new MessageHeaders(headersMap);
        Message<Object> message = MessageBuilder.createMessage(body, messageHeaders);

        setMessageConverter(rocketTemplate.getMessageConverter());
        Message<?> msg = super.doConvert(message.getPayload(), message.getHeaders(), null);
        org.apache.rocketmq.common.message.Message rocketMessage = RocketMQUtil.convertToRocketMessage(rocketTemplate.getMessageConverter(), "UTF-8",
                topic, msg);

        String hashKey = null;
        if (mqConfig != null) {
            if (mqConfig.getTag() != null) {
                rocketMessage.setTags(mqConfig.getTag());
            }
            hashKey = mqConfig.getHashKey();
            if (mqConfig.getDelayLevel() != null) {
                rocketMessage.setDelayTimeLevel(mqConfig.getDelayLevel().getCode());
            }
        }


        SendResult result;
        if (Objects.isNull(hashKey) || hashKey.isEmpty()) {
            result = producer.send(rocketMessage, producer.getSendMsgTimeout());
        } else {
            result = producer.send(rocketMessage, rocketTemplate.getMessageQueueSelector(), hashKey, producer.getSendMsgTimeout());
        }
        log.info("send result {}", result);
        return null;
    }


    private Topic getMessageQueue(Object proxy) {
        Topic messageQueue;
        if ((messageQueue = MESSAGE_QUEUE_MAP.get(proxy.getClass())) != null) {
            return messageQueue;
        }
        Type[] genericInterfaces = proxy.getClass().getGenericInterfaces();
        Class<?> genericInterface = (Class<?>) genericInterfaces[0];
        Topic annotation = genericInterface.getAnnotation(Topic.class);
        MESSAGE_QUEUE_MAP.put(proxy.getClass(), annotation);
        return annotation;
    }
}

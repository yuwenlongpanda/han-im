package com.ywl.framework.mq;

import com.ywl.framework.common.mq.MqMessage;
import com.ywl.framework.common.mq.annotation.Topic;
import com.ywl.framework.common.utils.SpringContextUtil;
import com.ywl.framework.mq.annotation.ConsumerGroup;
import lombok.var;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;
import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author liao
 */
@Configuration
public class ConsumerContainerConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

    private final static Logger log = LoggerFactory.getLogger(ConsumerContainerConfiguration.class);

    private ConfigurableApplicationContext applicationContext;

    private AtomicLong counter = new AtomicLong(0);

    private StandardEnvironment environment;

    private RocketMQProperties rocketMQProperties;

    private RocketMQMessageConverter rocketMQMessageConverter;

    public ConsumerContainerConfiguration(RocketMQMessageConverter rocketMQMessageConverter, StandardEnvironment environment, RocketMQProperties rocketMQProperties) {
        this.rocketMQMessageConverter = rocketMQMessageConverter;
        this.environment = environment;
        this.rocketMQProperties = rocketMQProperties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(ConsumerGroup.class).entrySet().stream()
                .filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        beans.forEach(this::registerContainer);
    }

    private RocketMQMessageListener instanceRocketMessageListener(String topic, String consumerGroup, String tag, ConsumeMode consumeMode, MessageModel messageModel) {
        //获取注解默认属性
        AnnotationType instance = AnnotationType.getInstance(RocketMQMessageListener.class);
        Map<String, Object> stringObjectMap = instance.memberDefaults();
        // 完善注解必填属性
        stringObjectMap.put("topic", topic);
        stringObjectMap.put("consumerGroup", consumerGroup);
        stringObjectMap.put("selectorExpression", tag);
        stringObjectMap.put("consumeMode", consumeMode);
        stringObjectMap.put("messageModel", messageModel);
        //反射生成实例
        return (RocketMQMessageListener) AnnotationParser.annotationForMap(RocketMQMessageListener.class, stringObjectMap);
    }

    @SuppressWarnings("unchecked")
    private void registerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (!MqMessage.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + MqMessage.class.getName());
        }

        ConsumerGroup consumerGroupAnnotation = clazz.getAnnotation(ConsumerGroup.class);
        String groupId = SpringContextUtil.getSpringValue(consumerGroupAnnotation.groupId());

        ConsumeMode consumeMode = consumerGroupAnnotation.consumeMode();

        MessageModel messageModel = consumerGroupAnnotation.messageModel();

        String tag = consumerGroupAnnotation.tag();

        Type genericInterface = clazz.getGenericInterfaces()[0];
        Class<?> parameterizedType = (Class<?>) genericInterface;
        Topic messageQueueAnnotation = parameterizedType.getAnnotation(Topic.class);

        String topic = this.environment.resolvePlaceholders(messageQueueAnnotation.value());

        String consumerGroup = topic.concat("-").concat(this.environment.resolvePlaceholders(groupId));

        RocketMQMessageListener annotation = instanceRocketMessageListener(topic, consumerGroup, tag, consumeMode, messageModel);

        boolean listenerEnabled = (boolean) rocketMQProperties.getConsumer().getListeners().getOrDefault(consumerGroup, Collections.EMPTY_MAP).getOrDefault(topic, true);

        if (!listenerEnabled) {
            log.debug("Consumer Listener (group:{},topic:{}) is not enabled by configuration, will ignore initialization.", consumerGroup, topic);
            return;
        }
        validate(annotation);

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(), counter.incrementAndGet());
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        MqMessage<Object> messageContext = (MqMessage<Object>) bean;
        RocketMQListener<Object> rocketListener = messageContext::consumer;


        genericApplicationContext.registerBean(containerBeanName, DefaultRocketMQListenerContainer.class, () -> createRocketMQListenerContainer(containerBeanName, rocketListener, annotation));
        DefaultRocketMQListenerContainer container = genericApplicationContext.getBean(containerBeanName, DefaultRocketMQListenerContainer.class);
        if (!container.isRunning()) {
            try {
                ResolvableType resolvableType = ResolvableType.forClass(clazz);
                ResolvableType generic = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0);
                Type type = generic.getType();

                Field messageType = container.getClass().getDeclaredField("messageType");
                messageType.setAccessible(true);
                messageType.set(container, type);

                container.start();
            } catch (Exception e) {
                log.error("Started container failed. {}", container, e);
                throw new RuntimeException(e);
            }
        }

        log.info("Register the listener to container, listenerBeanName:{}, containerBeanName:{}", beanName, containerBeanName);
    }

    private DefaultRocketMQListenerContainer createRocketMQListenerContainer(String name, Object bean, RocketMQMessageListener annotation) {
        DefaultRocketMQListenerContainer container = new DefaultRocketMQListenerContainer();
        container.setRocketMQMessageListener(annotation);

        String nameServer = environment.resolvePlaceholders(annotation.nameServer());
        nameServer = StringUtils.isEmpty(nameServer) ? rocketMQProperties.getNameServer() : nameServer;
        String accessChannel = environment.resolvePlaceholders(annotation.accessChannel());
        container.setNameServer(nameServer);
        if (!StringUtils.isEmpty(accessChannel)) {
            container.setAccessChannel(AccessChannel.valueOf(accessChannel));
        }
        container.setTopic(environment.resolvePlaceholders(annotation.topic()));
        String tags = environment.resolvePlaceholders(annotation.selectorExpression());
        if (!StringUtils.isEmpty(tags)) {
            container.setSelectorExpression(tags);
        }
        container.setConsumerGroup(environment.resolvePlaceholders(annotation.consumerGroup()));
        //老版本没有这个字段，暂时先注释掉
//        container.setTlsEnable(environment.resolvePlaceholders(annotation.tlsEnable()));
        if (RocketMQListener.class.isAssignableFrom(bean.getClass())) {
            container.setRocketMQListener((RocketMQListener<?>) bean);
        } else if (RocketMQReplyListener.class.isAssignableFrom(bean.getClass())) {
            container.setRocketMQReplyListener((RocketMQReplyListener<?, ?>) bean);
        }
        container.setMessageConverter(rocketMQMessageConverter.getMessageConverter());
        container.setName(name);

        return container;
    }

    private void validate(RocketMQMessageListener annotation) {
        if (annotation.consumeMode() == ConsumeMode.ORDERLY &&
                annotation.messageModel() == MessageModel.BROADCASTING) {
            throw new BeanDefinitionValidationException(
                    "Bad annotation definition in @RocketMQMessageListener, messageModel BROADCASTING does not support ORDERLY message!");
        }
    }


}

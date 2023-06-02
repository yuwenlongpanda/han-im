package com.ywl.framework.mq;

import com.ywl.framework.common.mq.annotation.Topic;
import com.ywl.framework.mq.annotation.MessageQueueScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;

public class MessageQueueRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MessageQueueScan.class.getName());
        ClassPathMessageQueueScanner classPathMessageQueueScanner = new ClassPathMessageQueueScanner(registry);
        classPathMessageQueueScanner.addIncludeFilter(new AnnotationTypeFilter(Topic.class));
        classPathMessageQueueScanner.scan((String []) annotationAttributes.get("value"));
    }
}

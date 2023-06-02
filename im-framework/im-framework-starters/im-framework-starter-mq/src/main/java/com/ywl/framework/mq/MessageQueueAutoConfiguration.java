package com.ywl.framework.mq;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ywl.framework.mq.annotation.MessageQueueScan;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Configuration
@MessageQueueScan("com.ywl.**.mq.**")
@Import(ConsumerContainerConfiguration.class)
public class MessageQueueAutoConfiguration {

    /**
     * 覆盖原有的rocketmq 序列化
     * @return
     */
    @Primary
    @Bean
    public RocketMQMessageConverter customRocketMessageConverter() {
        RocketMQMessageConverter rocketMessageConverter = new RocketMQMessageConverter();
        CompositeMessageConverter messageConverter = (CompositeMessageConverter) rocketMessageConverter.getMessageConverter();
        Optional<MessageConverter> messageConverterOptional = messageConverter.getConverters().stream()
                .filter(messageConverter1 -> messageConverter1.getClass()
                        .equals(MappingJackson2MessageConverter.class)).findAny();
        messageConverterOptional.ifPresent(messageConverter12 -> {
            MappingJackson2MessageConverter mappingFastJsonMessageConverter = (MappingJackson2MessageConverter) messageConverter12;
            ObjectMapper objectMapper = mappingFastJsonMessageConverter.getObjectMapper();
            JavaTimeModule timeModule = new JavaTimeModule();
            timeModule.addDeserializer(LocalDate.class,
                    new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            timeModule.addDeserializer(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            timeModule.addSerializer(LocalDate.class,
                    new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            timeModule.addSerializer(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            objectMapper.registerModule(timeModule);
        });
        return rocketMessageConverter;
    }

}

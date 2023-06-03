package com.ywl.im.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "im.thread-pool")
@Data
public class ThreadPoolProperties {
    private int coreSize;
    private int maxSize;
    private int keepAliveTime;
    private int queueSize;
    private String namePrefix;
}
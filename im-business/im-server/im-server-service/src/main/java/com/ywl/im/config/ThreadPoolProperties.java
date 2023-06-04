package com.ywl.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置属性类
 */
@Data
@ConfigurationProperties(prefix = "im.thread-pool")
public class ThreadPoolProperties {
    private int coreSize;          // 线程池核心线程数
    private int maxSize;           // 线程池最大线程数
    private int keepAliveTime;     // 非核心线程的空闲存活时间
    private int queueSize;         // 任务队列大小
    private String namePrefix;     // 线程名称前缀
}
